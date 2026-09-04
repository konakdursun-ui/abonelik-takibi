package com.example.aboneliktakibi;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONObject;

public class PaymentReminderReceiver extends BroadcastReceiver {
    public static final String CHANNEL_ID = "payment_reminders";
    public static final String ACTION_MARK_PAID = "com.example.aboneliktakibi.ACTION_MARK_PAID";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_ITEMS_KEY = "items_key";
    public static final String EXTRA_ITEM_ID = "item_id";
    public static final String EXTRA_DUE_YEAR = "due_year";
    public static final String EXTRA_DUE_MONTH = "due_month";
    public static final String EXTRA_DAY_OFFSET = "day_offset";
    public static final String EXTRA_NOTIFICATION_ID = "notification_id";
    private static final String PREFS = "subscription_tracker";
    private static final String KEY_LANGUAGE = "language";

    @Override
    public void onReceive(Context context, Intent intent) {
        createChannel(context);
        if (ACTION_MARK_PAID.equals(intent.getAction())) {
            markPaidFromNotification(context, intent);
            return;
        }
        String title = intent.getStringExtra(EXTRA_TITLE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        if (title == null) {
            title = fallbackTitle(context);
        }
        if (message == null) {
            message = fallbackMessage(context);
        }

        Intent launchIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(
                context,
                0,
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        int notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID,
                (int) (System.currentTimeMillis() % Integer.MAX_VALUE));
        PendingIntent paidIntent = PendingIntent.getBroadcast(
                context,
                markPaidRequestCode(intent),
                markPaidIntent(context, intent, notificationId),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        android.app.Notification.Builder builder = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? new android.app.Notification.Builder(context, CHANNEL_ID)
                : new android.app.Notification.Builder(context);

        builder.setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new android.app.Notification.BigTextStyle().bigText(message))
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setColor(Color.rgb(33, 128, 112))
                .addAction(android.R.drawable.checkbox_on_background, markPaidLabel(context), paidIntent);

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationId, builder.build());
    }

    private static Intent markPaidIntent(Context context, Intent source, int notificationId) {
        Intent intent = new Intent(context, PaymentReminderReceiver.class);
        intent.setAction(ACTION_MARK_PAID);
        intent.putExtra(EXTRA_ITEMS_KEY, source.getStringExtra(EXTRA_ITEMS_KEY));
        intent.putExtra(EXTRA_ITEM_ID, source.getLongExtra(EXTRA_ITEM_ID, 0));
        intent.putExtra(EXTRA_DUE_YEAR, source.getIntExtra(EXTRA_DUE_YEAR, 0));
        intent.putExtra(EXTRA_DUE_MONTH, source.getIntExtra(EXTRA_DUE_MONTH, -1));
        intent.putExtra(EXTRA_DAY_OFFSET, source.getIntExtra(EXTRA_DAY_OFFSET, 0));
        intent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);
        return intent;
    }

    private static int markPaidRequestCode(Intent intent) {
        long itemId = intent.getLongExtra(EXTRA_ITEM_ID, 0);
        int year = intent.getIntExtra(EXTRA_DUE_YEAR, 0);
        int month = intent.getIntExtra(EXTRA_DUE_MONTH, 0);
        long value = Math.abs(itemId) * 31L + year * 12L + month + 900_000L;
        return (int) (value % Integer.MAX_VALUE);
    }

    private static void markPaidFromNotification(Context context, Intent intent) {
        String itemsKey = intent.getStringExtra(EXTRA_ITEMS_KEY);
        long itemId = intent.getLongExtra(EXTRA_ITEM_ID, 0);
        int year = intent.getIntExtra(EXTRA_DUE_YEAR, 0);
        int month = intent.getIntExtra(EXTRA_DUE_MONTH, -1);
        if (itemsKey == null || itemsKey.trim().isEmpty() || itemId == 0 || year == 0 || month < 0) {
            return;
        }
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String json = prefs.getString(itemsKey, "[]");
        try {
            JSONArray array = new JSONArray(json);
            boolean changed = false;
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                if (item.optLong("id") != itemId) {
                    continue;
                }
                item.put("paid", true);
                item.put("paidMonth", month);
                item.put("paidYear", year);
                changed = true;
                break;
            }
            if (changed) {
                prefs.edit().putString(itemsKey, array.toString()).apply();
                cancelItemReminders(context, itemId);
                NotificationManager manager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                int notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 0);
                if (notificationId != 0 && manager != null) {
                    manager.cancel(notificationId);
                }
                showMarkedPaidConfirmation(context);
            }
        } catch (Exception ignored) {
        }
    }

    private static void cancelItemReminders(Context context, long itemId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) {
            return;
        }
        for (int dayOffset = 0; dayOffset <= 5; dayOffset++) {
            Intent reminderIntent = new Intent(context, PaymentReminderReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    reminderRequestCode(itemId, dayOffset),
                    reminderIntent,
                    PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE
            );
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
            }
        }
    }

    private static int reminderRequestCode(long itemId, int dayOffset) {
        long value = Math.abs(itemId) * 10L + dayOffset;
        return (int) (value % Integer.MAX_VALUE);
    }

    private static void showMarkedPaidConfirmation(Context context) {
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager == null) {
            return;
        }
        android.app.Notification.Builder builder = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? new android.app.Notification.Builder(context, CHANNEL_ID)
                : new android.app.Notification.Builder(context);
        builder.setSmallIcon(android.R.drawable.checkbox_on_background)
                .setContentTitle(markedPaidTitle(context))
                .setContentText(markedPaidMessage(context))
                .setAutoCancel(true)
                .setColor(Color.rgb(33, 128, 112));
        manager.notify((int) (System.currentTimeMillis() % Integer.MAX_VALUE), builder.build());
    }

    public static void createChannel(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                channelName(context),
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setDescription(channelDescription(context));
        manager.createNotificationChannel(channel);
    }

    private static String languageCode(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_LANGUAGE, "en");
    }

    private static String fallbackTitle(Context context) {
        String language = languageCode(context);
        if ("tr".equals(language)) return "Ödeme hatırlatıcısı";
        if ("de".equals(language)) return "Zahlungserinnerung";
        if ("es".equals(language)) return "Recordatorio de pago";
        if ("fr".equals(language)) return "Rappel de paiement";
        if ("pt".equals(language)) return "Lembrete de pagamento";
        return "Payment reminder";
    }

    private static String fallbackMessage(Context context) {
        String language = languageCode(context);
        if ("tr".equals(language)) return "Yaklaşan bir ödeme tarihin var.";
        if ("de".equals(language)) return "Eine Zahlung steht bald an.";
        if ("es".equals(language)) return "Tienes un pago próximo.";
        if ("fr".equals(language)) return "Un paiement approche.";
        if ("pt".equals(language)) return "Você tem um pagamento próximo.";
        return "You have an upcoming payment.";
    }

    private static String markPaidLabel(Context context) {
        String language = languageCode(context);
        if ("tr".equals(language)) return "Ödendi yap";
        if ("de".equals(language)) return "Als bezahlt markieren";
        if ("es".equals(language)) return "Marcar pagado";
        if ("fr".equals(language)) return "Marquer payé";
        if ("pt".equals(language)) return "Marcar como pago";
        return "Mark paid";
    }

    private static String markedPaidTitle(Context context) {
        String language = languageCode(context);
        if ("tr".equals(language)) return "Ödendi olarak işaretlendi";
        if ("de".equals(language)) return "Als bezahlt markiert";
        if ("es".equals(language)) return "Marcado como pagado";
        if ("fr".equals(language)) return "Marqué comme payé";
        if ("pt".equals(language)) return "Marcado como pago";
        return "Marked as paid";
    }

    private static String markedPaidMessage(Context context) {
        String language = languageCode(context);
        if ("tr".equals(language)) return "Kayıt uygulamaya girmeden güncellendi.";
        if ("de".equals(language)) return "Der Eintrag wurde ohne Öffnen der App aktualisiert.";
        if ("es".equals(language)) return "El registro se actualizó sin abrir la app.";
        if ("fr".equals(language)) return "L'enregistrement a été mis à jour sans ouvrir l'application.";
        if ("pt".equals(language)) return "O registro foi atualizado sem abrir o app.";
        return "The record was updated without opening the app.";
    }

    private static String channelName(Context context) {
        String language = languageCode(context);
        if ("tr".equals(language)) return "Ödeme hatırlatmaları";
        if ("de".equals(language)) return "Zahlungserinnerungen";
        if ("es".equals(language)) return "Recordatorios de pago";
        if ("fr".equals(language)) return "Rappels de paiement";
        if ("pt".equals(language)) return "Lembretes de pagamento";
        return "Payment reminders";
    }

    private static String channelDescription(Context context) {
        String language = languageCode(context);
        if ("tr".equals(language)) return "Yaklaşan abonelik ve gider ödemeleri";
        if ("de".equals(language)) return "Anstehende Abonnement- und Ausgabenzahlungen";
        if ("es".equals(language)) return "Pagos próximos de suscripciones y gastos";
        if ("fr".equals(language)) return "Paiements d'abonnements et de dépenses à venir";
        if ("pt".equals(language)) return "Pagamentos futuros de assinaturas e despesas";
        return "Upcoming subscription and expense payments";
    }
}
