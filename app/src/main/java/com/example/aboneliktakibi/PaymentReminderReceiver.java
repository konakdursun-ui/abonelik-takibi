package com.example.aboneliktakibi;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

public class PaymentReminderReceiver extends BroadcastReceiver {
    public static final String CHANNEL_ID = "payment_reminders";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_MESSAGE = "message";
    private static final String PREFS = "subscription_tracker";
    private static final String KEY_LANGUAGE = "language";

    @Override
    public void onReceive(Context context, Intent intent) {
        createChannel(context);
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

        android.app.Notification.Builder builder = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? new android.app.Notification.Builder(context, CHANNEL_ID)
                : new android.app.Notification.Builder(context);

        builder.setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setColor(Color.rgb(33, 128, 112));

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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
