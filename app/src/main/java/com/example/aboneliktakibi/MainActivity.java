package com.example.aboneliktakibi;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.BitmapFactory;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.WindowInsets;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import androidx.credentials.Credential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends Activity {
    private static int COLOR_BG = Color.rgb(248, 249, 250);
    private static int COLOR_SURFACE = Color.WHITE;
    private static int COLOR_SURFACE_LOW = Color.rgb(243, 244, 245);
    private static int COLOR_SURFACE_HIGH = Color.rgb(231, 232, 233);
    private static int COLOR_TEXT = Color.rgb(25, 28, 29);
    private static int COLOR_MUTED = Color.rgb(62, 73, 74);
    private static int COLOR_PRIMARY = Color.rgb(0, 83, 91);
    private static int COLOR_PRIMARY_CONTAINER = Color.rgb(0, 109, 119);
    private static int COLOR_ACCENT = Color.rgb(137, 81, 0);
    private static int COLOR_ACCENT_CONTAINER = Color.rgb(253, 157, 26);
    private static int COLOR_LINE = Color.rgb(190, 200, 202);
    private static final String PREFS = "subscription_tracker";
    private static final String KEY_ITEMS = "items";
    private static final String KEY_PROFILE_URI = "profile_uri";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_CURRENCY = "currency";
    private static final String KEY_THEME = "theme";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_COUNTRY_PROMPT_COMPLETED = "country_prompt_completed";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_AUTH_EMAIL = "auth_email";
    private static final String KEY_AUTH_PASSWORD = "auth_password";
    private static final String PROVIDER_EMAIL = "email";
    private static final String PROVIDER_GOOGLE = "google";
    private static final String KEY_CAMPAIGN_CACHE = "campaign_feed_cache";
    private static final String KEY_LAST_CLOUD_SYNC = "last_cloud_sync";
    private static final String CLOUD_PRIMARY_USERS = "users";
    private static final String CLOUD_USERS = "subscription_tracker_users";
    private static final String CLOUD_EMAIL_BACKUPS = "subscription_tracker_email_backups";
    private static final String CLOUD_BACKUPS = "backups";
    private static final String CLOUD_SUBSCRIPTIONS = "subscriptions";
    private static final String CAMPAIGN_FEED_URL = "https://raw.githubusercontent.com/konakdursun-ui/abonelik-takibi-kampanyalar/main/campaigns.json";
    private static final String[] CURRENCY_OPTIONS = {"USD", "EUR", "GBP", "TRY", "CAD", "AUD", "INR", "JPY"};
    private static final int REQUEST_PICK_PROFILE = 304;
    private static final int REQUEST_GOOGLE_SIGN_IN = 305;
    private static final int SCREEN_HOME = 0;
    private static final int SCREEN_CATEGORIES = 1;
    private static final int SCREEN_CALENDAR = 2;
    private static final int SCREEN_SETTINGS = 3;
    private static final int SCREEN_RECORD = 4;
    private static final int SCREEN_LOGIN = 5;
    private static final int SCREEN_SIGNUP = 6;
    private static final int SCREEN_EMAIL_LOGIN = 7;
    private static final int SCREEN_CAMPAIGNS = 8;
    private static final int TOP_AVATAR_SIZE_DP = 48;
    private static final String[] CATEGORIES = {
            "Abonelik", "Elektrik", "Su", "Doğalgaz", "İnternet", "Telefon", "Kira", "Ulaşım", "Diğer"
    };
    private static final Map<String, String> DE_TRANSLATIONS = new HashMap<>();
    private static final Map<String, String> ES_TRANSLATIONS = new HashMap<>();
    private static final Map<String, String> FR_TRANSLATIONS = new HashMap<>();
    private static final Map<String, String> PT_TRANSLATIONS = new HashMap<>();

    static {
        addTranslation("Subscription Tracker", "Abo-Tracker", "Gestor de suscripciones", "Suivi des abonnements", "Gerenciador de assinaturas");
        addTranslation("Track subscriptions, bills and monthly expenses in one place.", "Verfolge Abos, Rechnungen und monatliche Ausgaben an einem Ort.", "Controla suscripciones, facturas y gastos mensuales en un solo lugar.", "Suivez vos abonnements, factures et dépenses mensuelles au même endroit.", "Acompanhe assinaturas, contas e despesas mensais em um só lugar.");
        addTranslation("THIS MONTH\nTOTAL", "DIESER MONAT\nGESAMT", "TOTAL\nDEL MES", "TOTAL\nDU MOIS", "TOTAL\nDO MÊS");
        addTranslation("PAID", "BEZAHLT", "PAGADO", "PAYÉ", "PAGO");
        addTranslation("LEFT", "OFFEN", "RESTANTE", "RESTANT", "RESTANTE");
        addTranslation("New Record", "Neuer Eintrag", "Nuevo registro", "Nouvel enregistrement", "Novo registro");
        addTranslation("Category summary", "Kategorieübersicht", "Resumen por categoría", "Résumé par catégorie", "Resumo por categoria");
        addTranslation("Upcoming Payments", "Anstehende Zahlungen", "Pagos próximos", "Paiements à venir", "Pagamentos próximos");
        addTranslation("See All", "Alle ansehen", "Ver todo", "Tout voir", "Ver tudo");
        addTranslation("All records", "Alle Einträge", "Todos los registros", "Tous les enregistrements", "Todos os registros");
        addTranslation("All Records", "Alle Einträge", "Todos los registros", "Tous les enregistrements", "Todos os registros");
        addTranslation("Top Spending", "Höchste Ausgaben", "Mayor gasto", "Dépense principale", "Maior gasto");
        addTranslation("Detail", "Detail", "Detalle", "Détail", "Detalhe");
        addTranslation("Details", "Details", "Detalles", "Détails", "Detalhes");
        addTranslation("Deals & Campaigns", "Angebote & Kampagnen", "Ofertas y campañas", "Offres et campagnes", "Ofertas e campanhas");
        addTranslation("Smart Finance Summary", "Smarte Finanzübersicht", "Resumen financiero inteligente", "Résumé financier intelligent", "Resumo financeiro inteligente");
        addTranslation("Cloud sync on • ", "Cloud-Sync aktiv • ", "Sincronización en la nube activa • ", "Synchronisation cloud active • ", "Sincronização na nuvem ativa • ");
        addTranslation("Sign in with Google to enable cloud sync", "Mit Google anmelden, um Cloud-Sync zu aktivieren", "Inicia sesión con Google para activar la sincronización", "Connectez-vous avec Google pour activer la synchronisation", "Entre com Google para ativar a sincronização");
        addTranslation("Country and language", "Land und Sprache", "País e idioma", "Pays et langue", "País e idioma");
        addTranslation("Choose your country and app language to personalize your account.", "Wähle dein Land und die App-Sprache, um dein Konto anzupassen.", "Elige tu país y el idioma de la app para personalizar tu cuenta.", "Choisissez votre pays et la langue de l'application pour personnaliser votre compte.", "Escolha seu país e o idioma do app para personalizar sua conta.");
        addTranslation("COUNTRY", "LAND", "PAÍS", "PAYS", "PAÍS");
        addTranslation("LANGUAGE", "SPRACHE", "IDIOMA", "LANGUE", "IDIOMA");
        addTranslation("Continue", "Weiter", "Continuar", "Continuer", "Continuar");
        addTranslation("This month looks ", "Dieser Monat ist ", "Este mes parece ", "Ce mois-ci semble ", "Este mês parece ");
        addTranslation(" higher than last month.", " höher als letzter Monat.", " más alto que el mes pasado.", " plus élevé que le mois dernier.", " maior que o mês passado.");
        addTranslation(" lower than last month.", " niedriger als letzter Monat.", " más bajo que el mes pasado.", " plus bas que le mois dernier.", " menor que o mês passado.");
        addTranslation("Planned total payment this month is ", "Die geplante Zahlung in diesem Monat beträgt ", "El pago total previsto este mes es ", "Le total prévu ce mois-ci est de ", "O total planejado deste mês é ");
        addTranslation("Estimated total next month is ", "Die Schätzung für nächsten Monat beträgt ", "El total estimado del próximo mes es ", "Le total estimé du mois prochain est de ", "O total estimado do próximo mês é ");
        addTranslation("Within 7 days, ", "In 7 Tagen stehen ", "En 7 días hay ", "Dans 7 jours, ", "Em 7 dias, ");
        addTranslation(" payment(s) are coming up.", " Zahlung(en) an.", " pago(s) próximos.", " paiement(s) arrivent.", " pagamento(s) estão chegando.");
        addTranslation("No upcoming payment this week.", "Diese Woche steht keine Zahlung an.", "No hay pagos próximos esta semana.", "Aucun paiement prévu cette semaine.", "Nenhum pagamento próximo esta semana.");
        addTranslation("Take control of\nyour financial\nfreedom.", "Übernimm die Kontrolle\nüber deine finanzielle\nFreiheit.", "Toma el control\nde tu libertad\nfinanciera.", "Prenez le contrôle\nde votre liberté\nfinancière.", "Assuma o controle\nda sua liberdade\nfinanceira.");
        addTranslation("Continue with Google", "Mit Google fortfahren", "Continuar con Google", "Continuer avec Google", "Continuar com Google");
        addTranslation("Secure sign-in with your Google account", "Sichere Anmeldung mit deinem Google-Konto", "Inicio de sesión seguro con tu cuenta de Google", "Connexion sécurisée avec votre compte Google", "Login seguro com sua conta Google");
        addTranslation("Create Account", "Konto erstellen", "Crear cuenta", "Créer un compte", "Criar conta");
        addTranslation("Create an account to start tracking.", "Erstelle ein Konto, um mit dem Tracking zu beginnen.", "Crea una cuenta para empezar el seguimiento.", "Créez un compte pour commencer le suivi.", "Crie uma conta para começar a acompanhar.");
        addTranslation("Full Name", "Vollständiger Name", "Nombre completo", "Nom complet", "Nome completo");
        addTranslation("Email", "E-Mail", "Correo electrónico", "E-mail", "E-mail");
        addTranslation("Password", "Passwort", "Contraseña", "Mot de passe", "Senha");
        addTranslation("Sign Up", "Registrieren", "Registrarse", "S'inscrire", "Cadastrar");
        addTranslation("Sign up with Google", "Mit Google registrieren", "Registrarse con Google", "S'inscrire avec Google", "Cadastrar com Google");
        addTranslation("Already have an account?   Sign In", "Du hast schon ein Konto?   Anmelden", "¿Ya tienes una cuenta?   Iniciar sesión", "Vous avez déjà un compte ?   Connexion", "Já tem uma conta?   Entrar");
        addTranslation("Sign In", "Anmelden", "Iniciar sesión", "Connexion", "Entrar");
        addTranslation("Continue with your registered email and password.", "Mit registrierter E-Mail und Passwort fortfahren.", "Continúa con tu correo y contraseña registrados.", "Continuez avec votre e-mail et mot de passe enregistrés.", "Continue com seu e-mail e senha cadastrados.");
        addTranslation("No account?   Sign up", "Kein Konto?   Registrieren", "¿No tienes cuenta?   Regístrate", "Pas de compte ?   Inscription", "Sem conta?   Cadastre-se");
        addTranslation("Categories", "Kategorien", "Categorías", "Catégories", "Categorias");
        addTranslation("Estimated ", "Geschätzte ", "Estimado ", "Estimé ", "Estimado ");
        addTranslation(" Category Expenses", " Kategorieausgaben", " gastos por categoría", " dépenses par catégorie", " despesas por categoria");
        addTranslation("No categorized records this month.", "Keine kategorisierten Einträge in diesem Monat.", "No hay registros categorizados este mes.", "Aucun enregistrement catégorisé ce mois-ci.", "Nenhum registro categorizado este mês.");
        addTranslation(" records", " Einträge", " registros", " enregistrements", " registros");
        addTranslation("Monthly Expense Chart", "Monatliches Ausgabendiagramm", "Gráfico de gastos mensuales", "Graphique des dépenses mensuelles", "Gráfico de despesas mensais");
        addTranslation("Past/Future 6 months", "Vergangenheit/Zukunft 6 Monate", "Pasado/Futuro 6 meses", "Passé/Futur 6 mois", "Passado/Futuro 6 meses");
        addTranslation("Past/Future 1 year", "Vergangenheit/Zukunft 1 Jahr", "Pasado/Futuro 1 año", "Passé/Futur 1 an", "Passado/Futuro 1 ano");
        addTranslation("Past/Future 3 years", "Vergangenheit/Zukunft 3 Jahre", "Pasado/Futuro 3 años", "Passé/Futur 3 ans", "Passado/Futuro 3 anos");
        addTranslation("Chart Time Range", "Diagrammzeitraum", "Rango del gráfico", "Plage du graphique", "Intervalo do gráfico");
        addTranslation("Cancel", "Abbrechen", "Cancelar", "Annuler", "Cancelar");
        addTranslation("Total: ", "Gesamt: ", "Total: ", "Total : ", "Total: ");
        addTranslation("No payment for the selected day.", "Keine Zahlung für den ausgewählten Tag.", "No hay pagos para el día seleccionado.", "Aucun paiement pour le jour sélectionné.", "Nenhum pagamento para o dia selecionado.");
        addTranslation("All Payments This Month", "Alle Zahlungen diesen Monat", "Todos los pagos de este mes", "Tous les paiements de ce mois", "Todos os pagamentos deste mês");
        addTranslation("This Month", "Dieser Monat", "Este mes", "Ce mois-ci", "Este mês");
        addTranslation("No payments visible this month.", "Keine Zahlungen in diesem Monat sichtbar.", "No hay pagos visibles este mes.", "Aucun paiement visible ce mois-ci.", "Nenhum pagamento visível este mês.");
        addTranslation("Settings", "Einstellungen", "Ajustes", "Paramètres", "Configurações");
        addTranslation("Account", "Konto", "Cuenta", "Compte", "Conta");
        addTranslation("Personal Info", "Persönliche Daten", "Información personal", "Informations personnelles", "Informações pessoais");
        addTranslation("Notifications", "Benachrichtigungen", "Notificaciones", "Notifications", "Notificações");
        addTranslation("On", "Ein", "Activado", "Activé", "Ativo");
        addTranslation("Off", "Aus", "Desactivado", "Désactivé", "Desativado");
        addTranslation("Preferences", "Einstellungen", "Preferencias", "Préférences", "Preferências");
        addTranslation("Currency", "Währung", "Moneda", "Devise", "Moeda");
        addTranslation("Country", "Land", "País", "Pays", "País");
        addTranslation("Theme", "Design", "Tema", "Thème", "Tema");
        addTranslation("Language", "Sprache", "Idioma", "Langue", "Idioma");
        addTranslation("Info", "Info", "Información", "Infos", "Informações");
        addTranslation("Security", "Sicherheit", "Seguridad", "Sécurité", "Segurança");
        addTranslation("Local records", "Lokale Einträge", "Registros locales", "Enregistrements locaux", "Registros locais");
        addTranslation("About", "Über", "Acerca de", "À propos", "Sobre");
        addTranslation("Session", "Sitzung", "Sesión", "Session", "Sessão");
        addTranslation("Sign Out", "Abmelden", "Cerrar sesión", "Déconnexion", "Sair");
        addTranslation("Edit Record", "Eintrag bearbeiten", "Editar registro", "Modifier l'enregistrement", "Editar registro");
        addTranslation("Ex: Netflix Premium", "Bsp.: Netflix Premium", "Ej.: Netflix Premium", "Ex. : Netflix Premium", "Ex.: Netflix Premium");
        addTranslation("Record Name", "Eintragsname", "Nombre del registro", "Nom de l'enregistrement", "Nome do registro");
        addTranslation("Amount", "Betrag", "Importe", "Montant", "Valor");
        addTranslation("Payment Date: ", "Zahlungsdatum: ", "Fecha de pago: ", "Date de paiement : ", "Data de pagamento: ");
        addTranslation("CATEGORY", "KATEGORIE", "CATEGORÍA", "CATÉGORIE", "CATEGORIA");
        addTranslation("Repeats monthly", "Wiederholt sich monatlich", "Se repite mensualmente", "Se répète chaque mois", "Repete mensalmente");
        addTranslation("Creates automatic monthly records", "Erstellt automatische monatliche Einträge", "Crea registros mensuales automáticos", "Crée des enregistrements mensuels automatiques", "Cria registros mensais automáticos");
        addTranslation("Enable reminder", "Erinnerung aktivieren", "Activar recordatorio", "Activer le rappel", "Ativar lembrete");
        addTranslation("Notify before payment at your chosen interval", "Vor der Zahlung im gewählten Intervall benachrichtigen", "Notifica antes del pago en el intervalo elegido", "Notifier avant le paiement selon l'intervalle choisi", "Notificar antes do pagamento no intervalo escolhido");
        addTranslation("Daily", "Täglich", "Diario", "Quotidien", "Diário");
        addTranslation("Weekly", "Wöchentlich", "Semanal", "Hebdomadaire", "Semanal");
        addTranslation("Monthly", "Monatlich", "Mensual", "Mensuel", "Mensal");
        addTranslation("Yearly", "Jährlich", "Anual", "Annuel", "Anual");
        addTranslation("Reminder frequency: ", "Erinnerungshäufigkeit: ", "Frecuencia de recordatorio: ", "Fréquence du rappel : ", "Frequência do lembrete: ");
        addTranslation("REMINDER FREQUENCY", "ERINNERUNGSHÄUFIGKEIT", "FRECUENCIA DE RECORDATORIO", "FRÉQUENCE DU RAPPEL", "FREQUÊNCIA DO LEMBRETE");
        addTranslation("SUBSCRIPTION END DATE", "ABO-ENDDATUM", "FECHA DE FIN DE SUSCRIPCIÓN", "DATE DE FIN D'ABONNEMENT", "DATA DE TÉRMINO DA ASSINATURA");
        addTranslation("Save", "Speichern", "Guardar", "Enregistrer", "Salvar");
        addTranslation("Delete", "Löschen", "Eliminar", "Supprimer", "Excluir");
        addTranslation("POPULAR SERVICES", "BELIEBTE DIENSTE", "SERVICIOS POPULARES", "SERVICES POPULAIRES", "SERVIÇOS POPULARES");
        addTranslation("Pick popular services for your region and auto-fill the record.", "Wähle beliebte Dienste für deine Region und fülle den Eintrag automatisch aus.", "Elige servicios populares de tu región y completa el registro automáticamente.", "Choisissez des services populaires de votre région et remplissez automatiquement l'enregistrement.", "Escolha serviços populares da sua região e preencha o registro automaticamente.");
        addTranslation("Plans", "Tarife", "Planes", "Formules", "Planos");
        addTranslation("plans", "Tarife", "planes", "formules", "planos");
        addTranslation("enter current price", "aktuellen Preis eingeben", "introduce el precio actual", "saisir le prix actuel", "inserir preço atual");
        addTranslation("Plan selected. Check the date and save.", "Tarif ausgewählt. Datum prüfen und speichern.", "Plan seleccionado. Revisa la fecha y guarda.", "Formule sélectionnée. Vérifiez la date et enregistrez.", "Plano selecionado. Verifique a data e salve.");
        addTranslation("Plan selected. Enter the current price shown by the service.", "Tarif ausgewählt. Gib den aktuellen Preis des Dienstes ein.", "Plan seleccionado. Introduce el precio actual mostrado por el servicio.", "Formule sélectionnée. Saisissez le prix actuel affiché par le service.", "Plano selecionado. Insira o preço atual mostrado pelo serviço.");
        addTranslation("Campaigns", "Kampagnen", "Campañas", "Campagnes", "Campanhas");
        addTranslation("Official source", "Offizielle Quelle", "Fuente oficial", "Source officielle", "Fonte oficial");
        addTranslation("Expired • ", "Abgelaufen • ", "Vencido • ", "Expiré • ", "Expirado • ");
        addTranslation("For you • ", "Für dich • ", "Para ti • ", "Pour vous • ", "Para você • ");
        addTranslation("Campaign", "Kampagne", "Campaña", "Campagne", "Campanha");
        addTranslation("Review terms", "Bedingungen prüfen", "Revisar condiciones", "Voir les conditions", "Ver termos");
        addTranslation("EXPIRED", "ABGELAUFEN", "VENCIDO", "EXPIRÉ", "EXPIRADO");
        addTranslation("OFFICIAL", "OFFIZIELL", "OFICIAL", "OFFICIEL", "OFICIAL");
        addTranslation("Open official campaign page  ›", "Offizielle Kampagnenseite öffnen  ›", "Abrir página oficial de la campaña  ›", "Ouvrir la page officielle de la campagne  ›", "Abrir página oficial da campanha  ›");
        addTranslation("Page could not be opened.", "Seite konnte nicht geöffnet werden.", "No se pudo abrir la página.", "La page n'a pas pu être ouverte.", "Não foi possível abrir a página.");
        addTranslation("Name and amount are required.", "Name und Betrag sind erforderlich.", "Nombre e importe son obligatorios.", "Le nom et le montant sont requis.", "Nome e valor são obrigatórios.");
        addTranslation("Check the amount.", "Betrag prüfen.", "Revisa el importe.", "Vérifiez le montant.", "Verifique o valor.");
        addTranslation("Change Photo", "Foto ändern", "Cambiar foto", "Changer la photo", "Alterar foto");
        addTranslation("Light", "Hell", "Claro", "Clair", "Claro");
        addTranslation("Dark", "Dunkel", "Oscuro", "Sombre", "Escuro");
        addTranslation("Notification Settings", "Benachrichtigungseinstellungen", "Ajustes de notificaciones", "Paramètres de notification", "Configurações de notificação");
        addTranslation("OK", "OK", "Aceptar", "OK", "OK");
        addTranslation("Version ", "Version ", "Versión ", "Version ", "Versão ");
        addTranslation("Your personal finance assistant for tracking subscriptions, bills and expenses.", "Dein persönlicher Finanzassistent für Abos, Rechnungen und Ausgaben.", "Tu asistente financiero personal para suscripciones, facturas y gastos.", "Votre assistant financier personnel pour suivre abonnements, factures et dépenses.", "Seu assistente financeiro pessoal para acompanhar assinaturas, contas e despesas.");
        addTranslation("No pending payment for the next 7 days.", "Keine offenen Zahlungen in den nächsten 7 Tagen.", "No hay pagos pendientes en los próximos 7 días.", "Aucun paiement en attente dans les 7 prochains jours.", "Nenhum pagamento pendente nos próximos 7 dias.");
        addTranslation("Other Records This Month", "Weitere Einträge in diesem Monat", "Otros registros de este mes", "Autres enregistrements ce mois-ci", "Outros registros deste mês");
        addTranslation("Past months on the left, estimated future months on the right • Swipe to review", "Vergangene Monate links, geschätzte zukünftige Monate rechts • Zum Prüfen wischen", "Meses pasados a la izquierda, meses futuros estimados a la derecha • Desliza para revisar", "Mois passés à gauche, mois futurs estimés à droite • Faites glisser pour consulter", "Meses anteriores à esquerda, meses futuros estimados à direita • Deslize para revisar");
        addTranslation("Today", "Heute", "Hoy", "Aujourd'hui", "Hoje");
        addTranslation(" days left", " Tage übrig", " días restantes", " jours restants", " dias restantes");
        addTranslation("No category total for this month yet.", "Noch keine Kategoriesumme für diesen Monat.", "Aún no hay total por categoría este mes.", "Aucun total par catégorie pour ce mois.", "Ainda não há total por categoria este mês.");
        addTranslation("No records this month. Add your first subscription or expense.", "Keine Einträge in diesem Monat. Füge dein erstes Abo oder deine erste Ausgabe hinzu.", "No hay registros este mes. Añade tu primera suscripción o gasto.", "Aucun enregistrement ce mois-ci. Ajoutez votre premier abonnement ou dépense.", "Nenhum registro este mês. Adicione sua primeira assinatura ou despesa.");
        addTranslation("Past average", "Vergangener Durchschnitt", "Promedio anterior", "Moyenne passée", "Média anterior");
        addTranslation("Mark pending", "Als offen markieren", "Marcar pendiente", "Marquer en attente", "Marcar pendente");
        addTranslation("Edit", "Bearbeiten", "Editar", "Modifier", "Editar");
        addTranslation("Actual amount", "Tatsächlicher Betrag", "Importe real", "Montant réel", "Valor real");
        addTranslation(" bill • ", " Rechnung • ", " factura • ", " facture • ", " conta • ");
        addTranslation("This is the final month; your plan is ending.", "Dies ist der letzte Monat; dein Tarif endet.", "Este es el último mes; tu plan termina.", "C'est le dernier mois ; votre formule se termine.", "Este é o último mês; seu plano está terminando.");
        addTranslation("Enter a valid amount", "Gib einen gültigen Betrag ein", "Introduce un importe válido", "Saisissez un montant valide", "Insira um valor válido");
        addTranslation("Final plan payment", "Letzte Tarifzahlung", "Pago final del plan", "Paiement final de la formule", "Pagamento final do plano");
        addTranslation("Mark paid", "Als bezahlt markieren", "Marcar pagado", "Marquer payé", "Marcar como pago");
        addTranslation("Status: ", "Status: ", "Estado: ", "Statut : ", "Status: ");
        addTranslation("Reminder: ", "Erinnerung: ", "Recordatorio: ", "Rappel : ", "Lembrete: ");
        addTranslation("End: ", "Ende: ", "Fin: ", "Fin : ", "Fim: ");
        addTranslation("No end date", "Kein Enddatum", "Sin fecha de fin", "Pas de date de fin", "Sem data de término");
        addTranslation("Close", "Schließen", "Cerrar", "Fermer", "Fechar");
        addTranslation("Reminder frequency", "Erinnerungshäufigkeit", "Frecuencia de recordatorio", "Fréquence du rappel", "Frequência do lembrete");
        addTranslation("End Date: ", "Enddatum: ", "Fecha de fin: ", "Date de fin : ", "Data de término: ");
        addTranslation("No end date (tap to select)", "Kein Enddatum (zum Auswählen tippen)", "Sin fecha de fin (toca para elegir)", "Pas de date de fin (touchez pour choisir)", "Sem data de término (toque para selecionar)");
        addTranslation("Subscription end date", "Abo-Enddatum", "Fecha de fin de suscripción", "Date de fin d'abonnement", "Data de término da assinatura");
        addTranslation("Select date", "Datum wählen", "Seleccionar fecha", "Choisir une date", "Selecionar data");
        addTranslation("Remove end date", "Enddatum entfernen", "Eliminar fecha de fin", "Supprimer la date de fin", "Remover data de término");
        addTranslation("New record", "Neuer Eintrag", "Nuevo registro", "Nouvel enregistrement", "Novo registro");
        addTranslation("Edit record", "Eintrag bearbeiten", "Editar registro", "Modifier l'enregistrement", "Editar registro");
        addTranslation("Delete record", "Eintrag löschen", "Eliminar registro", "Supprimer l'enregistrement", "Excluir registro");
        addTranslation(" will be deleted. Continue?", " wird gelöscht. Fortfahren?", " se eliminará. ¿Continuar?", " sera supprimé. Continuer ?", " será excluído. Continuar?");
        addTranslation(" will be permanently deleted. Continue?", " wird dauerhaft gelöscht. Fortfahren?", " se eliminará permanentemente. ¿Continuar?", " sera supprimé définitivement. Continuer ?", " será excluído permanentemente. Continuar?");
        addTranslation("Cancellation page could not be opened.", "Kündigungsseite konnte nicht geöffnet werden.", "No se pudo abrir la página de cancelación.", "La page d'annulation n'a pas pu être ouverte.", "Não foi possível abrir a página de cancelamento.");
        addTranslation(" payment reminder", " Zahlungserinnerung", " recordatorio de pago", " rappel de paiement", " lembrete de pagamento");
        addTranslation("Google sign-in", "Google-Anmeldung", "Inicio con Google", "Connexion Google", "Login com Google");
        addTranslation("Verifying account...", "Konto wird geprüft...", "Verificando cuenta...", "Vérification du compte...", "Verificando conta...");
        addTranslation("Google configuration is missing.", "Google-Konfiguration fehlt.", "Falta la configuración de Google.", "La configuration Google est manquante.", "A configuração do Google está ausente.");
        addTranslation("Google configuration is not ready.", "Google-Konfiguration ist nicht bereit.", "La configuración de Google no está lista.", "La configuration Google n'est pas prête.", "A configuração do Google não está pronta.");
        addTranslation("Firebase session is not ready.", "Firebase-Sitzung ist nicht bereit.", "La sesión de Firebase no está lista.", "La session Firebase n'est pas prête.", "A sessão do Firebase não está pronta.");
        addTranslation("Google sign-in could not be completed: ", "Google-Anmeldung konnte nicht abgeschlossen werden: ", "No se pudo completar el inicio con Google: ", "La connexion Google n'a pas pu être terminée : ", "Não foi possível concluir o login com Google: ");
        addTranslation("Google sign-in was cancelled: ", "Google-Anmeldung wurde abgebrochen: ", "El inicio con Google fue cancelado: ", "La connexion Google a été annulée : ", "O login com Google foi cancelado: ");
        addTranslation("Exit App", "App beenden", "Salir de la app", "Quitter l'application", "Sair do app");
        addTranslation("Do you want to exit the app?", "Möchtest du die App beenden?", "¿Quieres salir de la app?", "Voulez-vous quitter l'application ?", "Deseja sair do app?");
        addTranslation("Exit", "Beenden", "Salir", "Quitter", "Sair");
        addTranslation("Subscription", "Abo", "Suscripción", "Abonnement", "Assinatura");
        addTranslation("Electricity", "Strom", "Electricidad", "Électricité", "Eletricidade");
        addTranslation("Water", "Wasser", "Agua", "Eau", "Água");
        addTranslation("Natural Gas", "Erdgas", "Gas natural", "Gaz naturel", "Gás natural");
        addTranslation("Internet", "Internet", "Internet", "Internet", "Internet");
        addTranslation("Phone", "Telefon", "Teléfono", "Téléphone", "Telefone");
        addTranslation("Rent", "Miete", "Alquiler", "Loyer", "Aluguel");
        addTranslation("Transport", "Transport", "Transporte", "Transport", "Transporte");
        addTranslation("Groceries", "Lebensmittel", "Comestibles", "Courses", "Mercado");
        addTranslation("Other", "Sonstiges", "Otro", "Autre", "Outro");
        addTranslation("Home", "Start", "Inicio", "Accueil", "Início");
        addTranslation("Calendar", "Kalender", "Calendario", "Calendrier", "Calendário");
    }

    private static void addTranslation(String en, String de, String es, String fr, String pt) {
        DE_TRANSLATIONS.put(en, de);
        ES_TRANSLATIONS.put(en, es);
        FR_TRANSLATIONS.put(en, fr);
        PT_TRANSLATIONS.put(en, pt);
    }

    private final List<ExpenseItem> items = new ArrayList<>();
    private final NumberFormat amountFormat = NumberFormat.getNumberInstance(new Locale("tr", "TR"));
    private LinearLayout content;
    private TextView totalText;
    private TextView paidText;
    private TextView upcomingText;
    private LinearLayout upcomingList;
    private LinearLayout categoryList;
    private LinearLayout allItemsList;
    private LinearLayout bottomNav;
    private ScrollView mainScrollView;
    private int systemBottomInset;
    private int currentScreen = SCREEN_HOME;
    private int returnScreen = SCREEN_HOME;
    private final ArrayDeque<Integer> screenHistory = new ArrayDeque<>();
    private boolean suppressScreenHistory;
    private OnBackInvokedCallback systemBackCallback;
    private int selectedCalendarDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    private int categoryFilter = 0;
    private String selectedCategoryDetail;
    private int reportRangeMonths = 6;
    private final Calendar selectedReportMonth = Calendar.getInstance();
    private final Calendar displayedCalendar = Calendar.getInstance();
    private ExpenseItem editingItem;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private GoogleSignInClient googleClient;
    private String webClientId = "";
    private boolean applyingCloudData;
    private boolean pendingCloudLoad;
    private boolean pendingCountryPromptAfterCloud;
    private boolean cloudLoadInProgress;
    private boolean cloudSyncWarningShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            startApp();
        } catch (Throwable throwable) {
            showStartupFallback(throwable);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        backupBeforeAppLeaves();
    }

    @Override
    protected void onStop() {
        super.onStop();
        backupBeforeAppLeaves();
    }

    private void startApp() {
        applyThemeColors();
        amountFormat.setMinimumFractionDigits(2);
        amountFormat.setMaximumFractionDigits(2);
        selectedReportMonth.set(Calendar.DAY_OF_MONTH, 1);
        Window window = getWindow();
        window.setStatusBarColor(COLOR_BG);
        window.setNavigationBarColor(COLOR_SURFACE);
        PaymentReminderReceiver.createChannel(this);
        initializeFirebase();
        migrateLegacyProfileIfNeeded();
        if (isLoggedIn()) {
            loadItems();
        } else {
            items.clear();
        }
        setContentView(createContentView());
        registerSystemBackNavigation();
        suppressScreenHistory = true;
        showScreen(isLoggedIn() ? SCREEN_HOME : SCREEN_LOGIN);
        suppressScreenHistory = false;
        if (pendingCloudLoad) {
            pendingCloudLoad = false;
            loadCloudData();
        }
        if (isLoggedIn()) {
            requestNotificationPermissionIfNeeded();
            scheduleReminders();
        }
    }

    private void showStartupFallback(Throwable throwable) {
        try {
            LinearLayout fallback = new LinearLayout(this);
            fallback.setOrientation(LinearLayout.VERTICAL);
            fallback.setGravity(Gravity.CENTER);
            fallback.setPadding(dp(24), dp(24), dp(24), dp(24));
            fallback.setBackgroundColor(Color.rgb(248, 249, 250));
            TextView title = new TextView(this);
            title.setText("Abonelik Takibi");
            title.setTextSize(26);
            title.setTextColor(Color.rgb(0, 83, 91));
            title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            title.setGravity(Gravity.CENTER);
            fallback.addView(title, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView message = new TextView(this);
            message.setText("Uygulama güvenli modda açıldı. Lütfen uygulamayı kapatıp tekrar açın.");
            message.setTextSize(16);
            message.setTextColor(Color.rgb(62, 73, 74));
            message.setGravity(Gravity.CENTER);
            message.setPadding(0, dp(18), 0, dp(18));
            fallback.addView(message, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            Button retry = new Button(this);
            retry.setText("Tekrar dene");
            retry.setAllCaps(false);
            retry.setOnClickListener(v -> {
                try {
                    setContentView(createContentView());
                    registerSystemBackNavigation();
                    showScreen(SCREEN_LOGIN);
                } catch (Throwable ignored) {
                    Toast.makeText(this, ui("Uygulama güvenli modda kaldı.", "The app remained in safe mode."), Toast.LENGTH_SHORT).show();
                }
            });
            fallback.addView(retry, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, dp(54)));
            setContentView(fallback);
        } catch (Throwable ignored) {
            finish();
        }
    }

    private View createContentView() {
        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(COLOR_BG);
        root.setOnApplyWindowInsetsListener((view, insets) -> {
            systemBottomInset = Math.max(0, insets.getSystemWindowInsetBottom());
            updateBottomNavInsets();
            applyScreenLayout(currentScreen);
            return insets;
        });

        mainScrollView = new ScrollView(this);
        mainScrollView.setFillViewport(true);
        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(16), dp(8), dp(16), bottomContentPadding(false));
        mainScrollView.addView(content, new ScrollView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        FrameLayout.LayoutParams scrollParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        root.addView(mainScrollView, scrollParams);

        bottomNav = bottomNav();
        root.addView(bottomNav, bottomNavParams());

        return root;
    }

    private void showScreen(int screen) {
        if (screen == SCREEN_SIGNUP || screen == SCREEN_EMAIL_LOGIN) {
            screen = SCREEN_LOGIN;
        }
        if (!suppressScreenHistory && content != null && screen != currentScreen) {
            if (screenHistory.isEmpty() || screenHistory.peekLast() != currentScreen) {
                screenHistory.addLast(currentScreen);
            }
            while (screenHistory.size() > 12) {
                screenHistory.removeFirst();
            }
        }
        currentScreen = screen;
        content.removeAllViews();
        applyScreenLayout(screen);
        if (screen == SCREEN_HOME) {
            buildHomeScreen();
        } else if (screen == SCREEN_CATEGORIES) {
            buildCategoriesScreen();
        } else if (screen == SCREEN_CALENDAR) {
            buildCalendarScreen();
        } else if (screen == SCREEN_SETTINGS) {
            buildSettingsScreen();
        } else if (screen == SCREEN_LOGIN) {
            buildLoginScreen();
        } else if (screen == SCREEN_SIGNUP) {
            buildSignupScreen();
        } else if (screen == SCREEN_EMAIL_LOGIN) {
            buildEmailLoginScreen();
        } else if (screen == SCREEN_CAMPAIGNS) {
            buildCampaignsScreen();
        } else {
            buildRecordScreen();
        }
        if (bottomNav != null) {
            boolean authScreen = screen == SCREEN_LOGIN || screen == SCREEN_SIGNUP || screen == SCREEN_EMAIL_LOGIN;
            bottomNav.setVisibility((screen == SCREEN_RECORD || authScreen) ? View.GONE : View.VISIBLE);
        }
        refreshBottomNav();
    }

    private void applyScreenLayout(int screen) {
        boolean authScreen = screen == SCREEN_LOGIN || screen == SCREEN_SIGNUP || screen == SCREEN_EMAIL_LOGIN;
        boolean fullScreen = screen == SCREEN_RECORD || authScreen;
        if (mainScrollView != null) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mainScrollView.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            mainScrollView.setLayoutParams(params);
        }
        if (screen == SCREEN_RECORD) {
            content.setPadding(dp(16), dp(8), dp(16), dp(96));
        } else if (!authScreen) {
            content.setPadding(dp(16), dp(8), dp(16), bottomContentPadding(true));
        }
    }

    private void buildHomeScreen() {
        content.addView(topAppBar(ui("Abonelik Takibi", "Subscription Tracker"), true));

        LinearLayout stats = new LinearLayout(this);
        stats.setOrientation(LinearLayout.HORIZONTAL);
        stats.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        totalText = statCard(stats, ui("BU AY\nTOPLAM", "THIS MONTH\nTOTAL"), COLOR_PRIMARY);
        paidText = statCard(stats, ui("ÖDENEN", "PAID"), COLOR_PRIMARY_CONTAINER);
        upcomingText = statCard(stats, ui("KALAN", "LEFT"), COLOR_ACCENT);
        content.addView(stats);

        Button addRecord = primaryButton("+  " + ui("Yeni Kayıt", "New Record"));
        addRecord.setOnClickListener(v -> openRecordScreen(null));
        LinearLayout.LayoutParams addParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(56)
        );
        addParams.setMargins(0, dp(20), 0, dp(24));
        content.addView(addRecord, addParams);

        content.addView(sectionHeader(ui("Yaklaşan Ödemeler", "Upcoming Payments"), ui("Tümünü Gör", "See All")));
        upcomingList = listContainer();
        content.addView(upcomingList);

        content.addView(sectionTitle(ui("Bu Ayki Diğer Kayıtlar", "Other Records This Month")));
        allItemsList = listContainer();
        content.addView(allItemsList);

        content.addView(smartInsightCard());
        content.addView(homeCampaignStrip());
        render();
    }

    private View homeCampaignStrip() {
        LinearLayout section = new LinearLayout(this);
        section.setOrientation(LinearLayout.VERTICAL);

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.addView(text(ui("Fırsatlar & Kampanyalar", "Deals & Campaigns"), 16, COLOR_TEXT, Typeface.BOLD),
                new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        TextView all = text(ui("Tümünü Gör", "See All"), 13, COLOR_PRIMARY, Typeface.BOLD);
        all.setOnClickListener(v -> showScreen(SCREEN_CAMPAIGNS));
        header.addView(all);
        section.addView(header);

        HorizontalScrollView scroll = new HorizontalScrollView(this);
        scroll.setHorizontalScrollBarEnabled(false);
        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.HORIZONTAL);
        String cached = getSharedPreferences(PREFS, MODE_PRIVATE).getString(KEY_CAMPAIGN_CACHE, "");
        if (!renderCampaignFeed(list, cached, true)) {
            renderFallbackCampaignsForCountry(list, true);
        }
        section.setVisibility(list.getChildCount() == 0 ? View.GONE : View.VISIBLE);
        scroll.addView(list);
        LinearLayout.LayoutParams scrollParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(210));
        scrollParams.setMargins(0, dp(10), 0, dp(20));
        section.addView(scroll, scrollParams);
        loadRemoteCampaigns(list, true, section);
        return section;
    }

    private int cachedCampaignCount() {
        String cached = getSharedPreferences(PREFS, MODE_PRIVATE).getString(KEY_CAMPAIGN_CACHE, "");
        try {
            JSONArray campaigns = new JSONObject(cached).getJSONArray("campaigns");
            int active = 0;
            for (int i = 0; i < campaigns.length(); i++) {
                if (!"expired".equals(campaigns.getJSONObject(i).optString("status", "active"))) {
                    active++;
                }
            }
            return active;
        } catch (Exception ignored) {
            return 0;
        }
    }

    private View smartInsightCard() {
        LinearLayout card = formCard();
        card.setBackground(round(Color.rgb(6, 83, 90), dp(18), 0));
        card.setElevation(dp(8));

        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        top.addView(text(ui("Akıllı Finans Özeti", "Smart Finance Summary"), 18, Color.WHITE, Typeface.BOLD),
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        card.addView(top);

        for (String insight : buildSmartInsights()) {
            TextView row = text("• " + insight, 13, Color.rgb(232, 250, 252), Typeface.NORMAL);
            row.setPadding(0, dp(8), 0, 0);
            card.addView(row);
        }

        String cloud = firebaseAuth != null && firebaseAuth.getCurrentUser() != null
                ? ui("Bulut senkron açık • ", "Cloud sync on • ") + getUserEmail()
                : ui("Google ile giriş yapınca bulut senkron açılır", "Sign in with Google to enable cloud sync");
        TextView footer = text(cloud, 11, Color.rgb(176, 230, 235), Typeface.BOLD);
        footer.setPadding(0, dp(12), 0, 0);
        card.addView(footer);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, dp(18), 0, dp(20));
        card.setLayoutParams(params);
        return card;
    }

    private boolean isEnglish() {
        return "en".equals(getLanguageCode());
    }

    private String ui(String tr, String en) {
        String language = getLanguageCode();
        if ("tr".equals(language)) {
            return tr;
        }
        if ("de".equals(language)) {
            return translatedText(en, DE_TRANSLATIONS);
        }
        if ("es".equals(language)) {
            return translatedText(en, ES_TRANSLATIONS);
        }
        if ("fr".equals(language)) {
            return translatedText(en, FR_TRANSLATIONS);
        }
        if ("pt".equals(language)) {
            return translatedText(en, PT_TRANSLATIONS);
        }
        return en;
    }

    private String translatedText(String en, Map<String, String> translations) {
        String translated = translations.get(en);
        return translated == null ? en : translated;
    }
private String getLanguageCode() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        if (prefs.contains(KEY_LANGUAGE)) {
            return prefs.getString(KEY_LANGUAGE, "tr");
        }
        return "TR".equals(currentCountryCode()) ? "tr" : "en";
    }

    private String getLanguageName() {
        String language = getLanguageCode();
        if ("tr".equals(language)) return "Türkçe";
        if ("de".equals(language)) return "Deutsch";
        if ("es".equals(language)) return "Español";
        if ("fr".equals(language)) return "Français";
        if ("pt".equals(language)) return "Português";
        return "English";
    }

    private Locale appLocale() {
        String language = getLanguageCode();
        if ("tr".equals(language)) return new Locale("tr", "TR");
        if ("de".equals(language)) return Locale.GERMANY;
        if ("es".equals(language)) return new Locale("es", "ES");
        if ("fr".equals(language)) return Locale.FRANCE;
        if ("pt".equals(language)) return new Locale("pt", "BR");
        return Locale.US;
    }

    private String currentCountryCode() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String saved = prefs.getString(KEY_COUNTRY, "");
        if (saved != null && !saved.trim().isEmpty()) {
            return saved.toUpperCase(Locale.US);
        }
        String country = Locale.getDefault().getCountry();
        return country == null || country.trim().isEmpty() ? "TR" : country.toUpperCase(Locale.US);
    }

    private String currentCountryLabel() {
        String country = currentCountryCode();
        if ("TR".equals(country)) return ui("Türkiye", "Turkey");
        if ("US".equals(country)) return "United States";
        if ("GB".equals(country) || "UK".equals(country)) return "United Kingdom";
        if ("DE".equals(country)) return "Germany";
        if ("FR".equals(country)) return "France";
        return new Locale("", country).getDisplayCountry(Locale.getDefault());
    }

    private void ensureCountrySelectionPrompt() {
        if (isCountryPromptCompletedForCurrentUser()) {
            return;
        }

        showCountryLanguageSetupDialog();
    }

    private boolean isCountryPromptCompletedForCurrentUser() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String email = getUserEmail();
        if (email != null && !email.trim().isEmpty()) {
            return prefs.getBoolean(countryPromptCompletedKey(email), false);
        }
        return prefs.getBoolean(KEY_COUNTRY_PROMPT_COMPLETED, false);
    }

    private String countryPromptCompletedKey(String email) {
        return KEY_COUNTRY_PROMPT_COMPLETED + "_" + email.trim().toLowerCase(Locale.US);
    }

    private void showCountryLanguageSetupDialog() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        final String[] countryValues = {"TR", "US", "GB", "DE", "ES", "FR", "PT", "BR", "CA", "AU", "IN", "JP"};
        final String[] countryLabels = {
                "Türkiye",
                "United States",
                "United Kingdom",
                "Germany",
                "Spain",
                "France",
                "Portugal",
                "Brazil",
                "Canada",
                "Australia",
                "India",
                "Japan"
        };
        final String[] languageValues = {"en", "tr", "de", "es", "fr", "pt"};
        final String[] languageLabels = {"English", "Türkçe", "Deutsch", "Español", "Français", "Português"};

        LinearLayout body = new LinearLayout(this);
        body.setOrientation(LinearLayout.VERTICAL);
        body.setPadding(dp(22), dp(20), dp(22), dp(20));
        body.setBackground(round(COLOR_SURFACE, dp(18), 0));

        TextView title = text(ui("Ülke ve dil", "Country and language"), 22, COLOR_TEXT, Typeface.BOLD);
        body.addView(title);

        TextView message = text(ui("Hesabını kişiselleştirmek için ülke ve uygulama dilini seç.",
                "Choose your country and app language to personalize your account."), 14, COLOR_MUTED, Typeface.NORMAL);
        message.setPadding(0, dp(8), 0, dp(18));
        body.addView(message);

        body.addView(formLabel(ui("ÜLKE", "COUNTRY")));
        Spinner countrySpinner = setupSpinner(countryLabels);
        int countryIndex = indexOf(countryValues, currentCountryCode(), 0);
        countrySpinner.setSelection(countryIndex);
        body.addView(countrySpinner, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(54)));

        body.addView(spacer(dp(14)));
        body.addView(formLabel(ui("DİL", "LANGUAGE")));
        Spinner languageSpinner = setupSpinner(languageLabels);
        String defaultLanguage = prefs.contains(KEY_LANGUAGE) ? getLanguageCode() : ("TR".equals(currentCountryCode()) ? "tr" : "en");
        languageSpinner.setSelection(indexOf(languageValues, defaultLanguage, 0));
        body.addView(languageSpinner, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(54)));

        body.addView(spacer(dp(18)));
        Button continueButton = primaryButton(ui("Devam et", "Continue"));
        body.addView(continueButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(54)));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(body)
                .setCancelable(false)
                .create();
        continueButton.setOnClickListener(v -> {
            String countryCode = countryValues[countrySpinner.getSelectedItemPosition()];
            String languageCode = languageValues[languageSpinner.getSelectedItemPosition()];
            prefs.edit()
                    .putString(KEY_COUNTRY, countryCode)
                    .putString(KEY_LANGUAGE, languageCode)
                    .putString(KEY_CURRENCY, defaultCurrencyForCountry(countryCode))
                    .putBoolean(KEY_COUNTRY_PROMPT_COMPLETED, true)
                    .putBoolean(countryPromptCompletedKey(getUserEmail()), true)
                    .apply();
            syncToCloud();
            dialog.dismiss();
            rebuildCurrentScreen();
        });
        dialog.setOnShowListener(d -> {
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(round(Color.TRANSPARENT, dp(18), 0));
            }
        });
        dialog.show();
    }

    private String defaultCurrencyForCountry(String countryCode) {
        String country = countryCode == null ? "" : countryCode.toUpperCase(Locale.US);
        if ("TR".equals(country)) return "TRY";
        if ("US".equals(country)) return "USD";
        if ("GB".equals(country) || "UK".equals(country)) return "GBP";
        if ("CA".equals(country)) return "CAD";
        if ("AU".equals(country)) return "AUD";
        if ("IN".equals(country)) return "INR";
        if ("JP".equals(country)) return "JPY";
        if ("DE".equals(country) || "ES".equals(country) || "FR".equals(country)
                || "PT".equals(country) || "IT".equals(country) || "NL".equals(country)
                || "BE".equals(country) || "AT".equals(country) || "IE".equals(country)
                || "FI".equals(country) || "GR".equals(country)) {
            return "EUR";
        }
        return getCurrency();
    }

    private Spinner setupSpinner(String[] labels) {
        Spinner spinner = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setBackground(round(COLOR_SURFACE_LOW, dp(12), COLOR_LINE));
        spinner.setPadding(dp(10), 0, dp(10), 0);
        return spinner;
    }

    private int indexOf(String[] values, String value, int fallback) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].equalsIgnoreCase(value)) {
                return i;
            }
        }
        return fallback;
    }

    private List<String> buildSmartInsights() {
        List<String> insights = new ArrayList<>();
        Calendar thisMonth = Calendar.getInstance();
        thisMonth.set(Calendar.DAY_OF_MONTH, 1);
        Calendar previous = (Calendar) thisMonth.clone();
        previous.add(Calendar.MONTH, -1);
        Calendar next = (Calendar) thisMonth.clone();
        next.add(Calendar.MONTH, 1);
        double currentTotal = totalForMonth(thisMonth);
        double previousTotal = totalForMonth(previous);
        double nextTotal = totalForMonth(next);
        double diff = currentTotal - previousTotal;
        if (previousTotal > 0) {
            insights.add(ui("Bu ay geçen aya göre ", "This month looks ") + money(Math.abs(diff))
                    + (diff >= 0 ? ui(" daha fazla görünüyor.", " higher than last month.") : ui(" daha düşük görünüyor.", " lower than last month.")));
        } else {
            insights.add(ui("Bu ay planlanan toplam ödeme ", "Planned total payment this month is ") + money(currentTotal) + ".");
        }
        insights.add(ui("Önümüzdeki ay tahmini toplam ", "Estimated total next month is ") + money(nextTotal) + ".");

        Calendar now = Calendar.getInstance();
        int soon = 0;
        for (ExpenseItem item : items) {
            Calendar due = item.nextReminderDate(now);
            long days = daysBetween(now, due);
            if (days >= 0 && days <= 7 && !item.isPaidFor(due.get(Calendar.YEAR), due.get(Calendar.MONTH))) {
                soon++;
            }
        }
        insights.add(soon > 0
                ? ui("7 gün içinde ", "Within 7 days, ") + soon + ui(" ödeme yaklaşıyor.", " payment(s) are coming up.")
                : ui("Bu hafta yaklaşan ödeme görünmüyor.", "No upcoming payment this week."));

        String rising = strongestPriceIncrease();
        if (!rising.isEmpty()) {
            insights.add(rising);
        }
        String ending = nearestEndingSubscription();
        if (!ending.isEmpty()) {
            insights.add(ending);
        }
        if (insights.size() > 5) {
            return insights.subList(0, 5);
        }
        return insights;
    }

    private void buildLoginScreen() {
        content.setPadding(dp(24), dp(26), dp(24), dp(18));
        content.setBackgroundColor(Color.rgb(249, 249, 252));

        LinearLayout brand = new LinearLayout(this);
        brand.setGravity(Gravity.CENTER);
        brand.setOrientation(LinearLayout.HORIZONTAL);
        brand.addView(appMarkIcon(), new LinearLayout.LayoutParams(dp(58), dp(58)));
        TextView brandName = text("Abonelik Takibi", 25, COLOR_PRIMARY, Typeface.BOLD);
        brandName.setLetterSpacing(-0.015f);
        LinearLayout.LayoutParams brandNameParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        brandNameParams.setMargins(dp(16), 0, 0, 0);
        brand.addView(brandName, brandNameParams);
        content.addView(brand, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        SpaceLike(dp(82));

        TextView headline = text(ui("Finansal\nözgürlüğünü\nkontrol altına al.", "Take control of\nyour financial\nfreedom."), 31, COLOR_TEXT, Typeface.BOLD);
        headline.setGravity(Gravity.CENTER);
        headline.setLineSpacing(dp(2), 0.98f);
        headline.setLetterSpacing(-0.02f);
        headline.setPadding(0, 0, 0, dp(34));
        content.addView(headline, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setPadding(0, 0, 0, 0);
        panel.setBackgroundColor(Color.TRANSPARENT);

        View google = premiumAuthButton(
                ui("Google ile Devam Et", "Continue with Google"),
                ui("Google hesabınla güvenli giriş", "Secure sign-in with your Google account"),
                googleIconView(dp(48)),
                false
        );
        google.setOnClickListener(v -> openGoogleAccountPicker());
        LinearLayout.LayoutParams googleParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(78));
        googleParams.setMargins(0, 0, 0, 0);
        panel.addView(google, googleParams);

        content.addView(panel, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private View premiumAuthButton(String title, String subtitle, View icon, boolean filled) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(18), 0, dp(18), 0);
        row.setClickable(true);
        row.setFocusable(true);
        row.setElevation(dp(4));
        row.setBackground(round(
                filled ? COLOR_PRIMARY_CONTAINER : COLOR_SURFACE,
                dp(18),
                filled ? 0 : COLOR_LINE
        ));

        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(dp(50), dp(50));
        iconParams.setMargins(0, 0, dp(16), 0);
        row.addView(icon, iconParams);

        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setGravity(Gravity.CENTER_VERTICAL);
        TextView titleView = text(title, 17, filled ? Color.WHITE : COLOR_TEXT, Typeface.NORMAL);
        TextView subtitleView = text(subtitle, 13, filled ? Color.rgb(149, 226, 235) : COLOR_MUTED, Typeface.BOLD);
        subtitleView.setPadding(0, dp(2), 0, 0);
        copy.addView(titleView);
        copy.addView(subtitleView);
        row.addView(copy, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        TextView arrow = text("›", 34, filled ? Color.rgb(149, 226, 235) : COLOR_MUTED, Typeface.BOLD);
        arrow.setGravity(Gravity.CENTER);
        row.addView(arrow, new LinearLayout.LayoutParams(dp(24), ViewGroup.LayoutParams.MATCH_PARENT));
        return row;
    }

    private View appMarkIcon() {
        ImageView logo = new ImageView(this);
        logo.setImageResource(getResources().getIdentifier("ic_abonelik_logo_png", "drawable", getPackageName()));
        logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        logo.setBackgroundColor(Color.TRANSPARENT);
        logo.setPadding(0, 0, 0, 0);
        return logo;
    }

    private View premiumCircleIcon(String value, boolean filled) {
        TextView icon = text(value, 23, filled ? Color.WHITE : Color.WHITE, Typeface.BOLD);
        icon.setGravity(Gravity.CENTER);
        icon.setBackground(round(filled ? Color.rgb(0, 75, 83) : COLOR_PRIMARY, dp(28), 0));
        return icon;
    }

    private View googleIconView(int size) {
        FrameLayout holder = new FrameLayout(this);
        holder.setBackground(round(Color.rgb(243, 243, 246), dp(28), 0));
        ImageView image = new ImageView(this);
        image.setImageBitmap(googleIconBitmap(dp(28)));
        image.setScaleType(ImageView.ScaleType.CENTER);
        holder.addView(image, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        return holder;
    }

    private Bitmap googleIconBitmap(int size) {
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(Math.max(3, size / 7f));
        paint.setStrokeCap(Paint.Cap.SQUARE);
        RectF arc = new RectF(size * 0.18f, size * 0.18f, size * 0.82f, size * 0.82f);
        paint.setColor(Color.rgb(66, 133, 244));
        canvas.drawArc(arc, -38, 92, false, paint);
        paint.setColor(Color.rgb(52, 168, 83));
        canvas.drawArc(arc, 54, 82, false, paint);
        paint.setColor(Color.rgb(251, 188, 5));
        canvas.drawArc(arc, 136, 78, false, paint);
        paint.setColor(Color.rgb(234, 67, 53));
        canvas.drawArc(arc, 214, 108, false, paint);
        paint.setColor(Color.rgb(66, 133, 244));
        paint.setStrokeWidth(Math.max(3, size / 7f));
        canvas.drawLine(size * 0.52f, size * 0.50f, size * 0.82f, size * 0.50f, paint);
        canvas.drawLine(size * 0.82f, size * 0.50f, size * 0.74f, size * 0.66f, paint);
        return bitmap;
    }

    private void buildSignupScreen() {
        content.setPadding(dp(28), 0, dp(28), dp(28));
        content.addView(authTopBar(ui("Hesap Oluştur", "Create Account"), SCREEN_LOGIN));
        SpaceLike(dp(56));
        content.addView(centeredLogo(dp(92), dp(92)));

        TextView intro = text(ui("Finansal takibe başlamak için hesap oluştur.", "Create an account to start tracking."), 17, COLOR_TEXT, Typeface.BOLD);
        intro.setGravity(Gravity.CENTER);
        intro.setPadding(0, dp(22), 0, dp(28));
        content.addView(intro, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        EditText name = authField(ui("Ad Soyad", "Full Name"), "John Doe", "♙", false);
        EditText email = authField(ui("E-posta", "Email"), "example@mail.com", "✉", false);
        EditText password = authField(ui("Şifre", "Password"), "••••••••", "▣", true);
        content.addView(authFieldBlock(ui("Ad Soyad", "Full Name"), name));
        content.addView(authFieldBlock(ui("E-posta", "Email"), email));
        content.addView(authFieldBlock(ui("Şifre", "Password"), password));

        Button create = primaryButton(ui("Kayıt Ol", "Sign Up"));
        create.setOnClickListener(v -> createAccount(name, email, password));
        LinearLayout.LayoutParams createParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(58));
        createParams.setMargins(0, dp(10), 0, dp(22));
        content.addView(create, createParams);

        content.addView(orDivider());

        Button google = outlineButton("G  " + ui("Google ile Kaydol", "Sign up with Google"));
        google.setOnClickListener(v -> openGoogleAccountPicker());
        LinearLayout.LayoutParams googleParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(56));
        googleParams.setMargins(0, dp(22), 0, dp(26));
        content.addView(google, googleParams);

        TextView login = text(ui("Zaten hesabın var mı?   Giriş Yap", "Already have an account?   Sign In"), 14, COLOR_TEXT, Typeface.NORMAL);
        login.setGravity(Gravity.CENTER);
        login.setOnClickListener(v -> showScreen(SCREEN_EMAIL_LOGIN));
        content.addView(login, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void buildEmailLoginScreen() {
        content.setPadding(dp(28), 0, dp(28), dp(28));
        content.addView(authTopBar(ui("Giriş Yap", "Sign In"), SCREEN_LOGIN));
        SpaceLike(dp(64));
        content.addView(centeredLogo(dp(92), dp(92)));

        TextView intro = text(ui("Kayıtlı e-posta ve şifrenle devam et.", "Continue with your registered email and password."), 17, COLOR_TEXT, Typeface.BOLD);
        intro.setGravity(Gravity.CENTER);
        intro.setPadding(0, dp(22), 0, dp(32));
        content.addView(intro, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        EditText email = authField(ui("E-posta", "Email"), "example@mail.com", "✉", false);
        EditText password = authField(ui("Şifre", "Password"), "••••••••", "▣", true);
        content.addView(authFieldBlock(ui("E-posta", "Email"), email));
        content.addView(authFieldBlock(ui("Şifre", "Password"), password));

        Button login = primaryButton(ui("Giriş Yap", "Sign In"));
        login.setOnClickListener(v -> loginWithEmail(email, password));
        LinearLayout.LayoutParams loginParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(58));
        loginParams.setMargins(0, dp(12), 0, dp(24));
        content.addView(login, loginParams);

        TextView signup = text(ui("Hesabın yok mu?   Kaydol", "No account?   Sign up"), 14, COLOR_TEXT, Typeface.NORMAL);
        signup.setGravity(Gravity.CENTER);
        signup.setOnClickListener(v -> showScreen(SCREEN_SIGNUP));
        content.addView(signup, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private View authTopBar(String title, int backTarget) {
        LinearLayout bar = new LinearLayout(this);
        bar.setGravity(Gravity.CENTER_VERTICAL);
        bar.setPadding(0, dp(12), 0, dp(12));
        TextView back = text("‹", 34, COLOR_TEXT, Typeface.NORMAL);
        back.setGravity(Gravity.CENTER);
        back.setOnClickListener(v -> showScreen(backTarget));
        bar.addView(back, new LinearLayout.LayoutParams(dp(42), dp(50)));
        TextView titleView = text(title, 22, COLOR_PRIMARY, Typeface.BOLD);
        titleView.setGravity(Gravity.CENTER);
        bar.addView(titleView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        bar.addView(spacer(dp(42)), new LinearLayout.LayoutParams(dp(42), dp(1)));
        return bar;
    }

    private View centeredLogo(int width, int height) {
        LinearLayout wrap = new LinearLayout(this);
        wrap.setGravity(Gravity.CENTER);
        wrap.addView(appMarkIcon(), new LinearLayout.LayoutParams(width, height));
        return wrap;
    }

    private View authFieldBlock(String label, EditText field) {
        LinearLayout block = new LinearLayout(this);
        block.setOrientation(LinearLayout.VERTICAL);
        block.addView(formLabel(label));
        block.addView(field, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(56)));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, dp(18));
        block.setLayoutParams(params);
        return block;
    }

    private EditText authField(String label, String hint, String icon, boolean password) {
        EditText editText = field(hint);
        editText.setTextSize(15);
        editText.setPadding(dp(18), dp(8), dp(18), dp(8));
        editText.setInputType(password
                ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        if ("E-posta".equals(label) || "Email".equals(label)) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }
        editText.setContentDescription(label);
        return editText;
    }

    private View orDivider() {
        LinearLayout row = new LinearLayout(this);
        row.setGravity(Gravity.CENTER);
        View left = new View(this);
        left.setBackgroundColor(COLOR_LINE);
        View right = new View(this);
        right.setBackgroundColor(COLOR_LINE);
        TextView label = text("veya", 12, COLOR_MUTED, Typeface.NORMAL);
        label.setGravity(Gravity.CENTER);
        row.addView(left, new LinearLayout.LayoutParams(0, dp(1), 1));
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(dp(72), ViewGroup.LayoutParams.WRAP_CONTENT);
        row.addView(label, labelParams);
        row.addView(right, new LinearLayout.LayoutParams(0, dp(1), 1));
        return row;
    }

    private View loginMockupCard() {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(16), dp(16), dp(16));
        card.setBackground(cardBg());
        card.setElevation(dp(8));

        LinearLayout preview = new LinearLayout(this);
        preview.setOrientation(LinearLayout.VERTICAL);
        preview.setPadding(dp(20), dp(18), dp(20), dp(18));
        preview.setBackground(round(Color.rgb(10, 55, 51), dp(12), 0));
        preview.addView(mockLine(dp(180), Color.rgb(71, 143, 135)));
        preview.addView(mockLine(dp(260), Color.rgb(50, 111, 105)));
        preview.addView(mockLine(dp(230), Color.rgb(94, 183, 174)));
        preview.addView(mockLine(dp(280), Color.rgb(34, 82, 78)));
        card.addView(preview, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(122)));

        card.addView(spacer(dp(16)));
        card.addView(mockLine(dp(210), Color.rgb(220, 224, 226)));
        card.addView(mockLine(ViewGroup.LayoutParams.MATCH_PARENT, Color.rgb(220, 224, 226)));
        card.addView(mockLine(dp(170), Color.rgb(128, 211, 222)));
        return card;
    }

    private View mockLine(int width, int color) {
        View line = new View(this);
        line.setBackground(round(color, dp(8), 0));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, dp(8));
        params.setMargins(0, dp(6), 0, dp(6));
        line.setLayoutParams(params);
        return line;
    }

    private void SpaceLike(int height) {
        content.addView(spacer(height));
    }

    private void initializeFirebase() {
        try {
            FirebaseApp app = FirebaseApp.initializeApp(this);
            if (app == null) {
                app = FirebaseApp.getInstance();
            }
            int webClientResId = getResources().getIdentifier("default_web_client_id", "string", getPackageName());
            webClientId = webClientResId == 0 ? "" : getString(webClientResId).trim();
            firebaseAuth = FirebaseAuth.getInstance(app);
            firestore = FirebaseFirestore.getInstance(app);
            firestore.setFirestoreSettings(new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build());
            storage = FirebaseStorage.getInstance(app);
            if (webClientId.isEmpty()) {
                Toast.makeText(this, ui("Google yapılandırması eksik.", "Google configuration is missing."), Toast.LENGTH_LONG).show();
            }
            if (firebaseAuth.getCurrentUser() != null) {
                finishFirebaseLogin(firebaseAuth.getCurrentUser(), false);
            } else {
                clearStaleLocalSession();
            }
        } catch (Throwable e) {
            firebaseAuth = null;
            firestore = null;
            storage = null;
            clearStaleLocalSession();
            Toast.makeText(this, ui("Bulut girişi geçici olarak kapalı. Uygulama yerel modda açılıyor.", "Cloud sign-in is temporarily unavailable. The app is opening in local mode."), Toast.LENGTH_LONG).show();
        }
    }

    private void clearStaleLocalSession() {
        getSharedPreferences(PREFS, MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_LOGGED_IN, false)
                .remove(KEY_USER_EMAIL)
                .apply();
    }

    private void completeLogin() {
        getSharedPreferences(PREFS, MODE_PRIVATE).edit().putBoolean(KEY_LOGGED_IN, true).apply();
        screenHistory.clear();
        content.setPadding(dp(16), dp(8), dp(16), dp(104));
        showScreenFromBack(SCREEN_HOME);
    }

    private void completeLogin(String name, String email) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS, MODE_PRIVATE).edit()
                .putBoolean(KEY_LOGGED_IN, true);
        if (name != null && !name.trim().isEmpty()) {
            editor.putString(KEY_USER_NAME, name.trim());
        }
        if (email != null && !email.trim().isEmpty()) {
            editor.putString(KEY_USER_EMAIL, email.trim());
        }
        editor.apply();
        migrateLegacyProfileIfNeeded();
        loadItems();
        requestNotificationPermissionIfNeeded();
        scheduleReminders();
        screenHistory.clear();
        content.setPadding(dp(16), dp(8), dp(16), dp(104));
        showScreenFromBack(SCREEN_HOME);
    }

    private void finishFirebaseLogin(FirebaseUser user) {
        finishFirebaseLogin(user, true);
    }

    private void finishFirebaseLogin(FirebaseUser user, boolean navigateHome) {
        if (user == null) {
            return;
        }
        cloudSyncWarningShown = false;
        String email = user.getEmail() == null ? "" : user.getEmail().trim();
        String name = user.getDisplayName();
        if (name == null || name.trim().isEmpty()) {
            name = email.contains("@") ? email.substring(0, email.indexOf('@')) : ui("Google Kullanıcısı", "Google User");
        }
        SharedPreferences.Editor editor = getSharedPreferences(PREFS, MODE_PRIVATE)
                .edit()
                .putString(authNameKey(email), name)
                .putString(authProviderKey(email), PROVIDER_GOOGLE)
                .putBoolean(KEY_LOGGED_IN, true);
        if (!name.trim().isEmpty()) {
            editor.putString(KEY_USER_NAME, name.trim());
        }
        if (!email.isEmpty()) {
            editor.putString(KEY_USER_EMAIL, email);
        }
        if (user.getPhotoUrl() != null && !email.isEmpty()) {
            editor.putString(profileUriKey(email), user.getPhotoUrl().toString());
        }
        editor.apply();
        migrateLegacyProfileIfNeeded();
        loadItems();
        scheduleReminders();
        if (content == null) {
            pendingCloudLoad = true;
        } else {
            loadCloudData();
        }
        if (navigateHome && content != null) {
            screenHistory.clear();
            content.setPadding(dp(16), dp(8), dp(16), dp(132));
            showScreenFromBack(SCREEN_HOME);
        }
    }

    private void createAccount(EditText name, EditText email, EditText password) {
        String cleanName = name.getText().toString().trim();
        String cleanEmail = email.getText().toString().trim();
        String cleanPassword = password.getText().toString();
        if (cleanName.isEmpty() || cleanEmail.isEmpty() || cleanPassword.isEmpty()) {
            Toast.makeText(this, ui("Ad, e-posta ve şifre gerekli.", "Name, email and password are required."), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!cleanEmail.contains("@") || cleanPassword.length() < 6) {
            Toast.makeText(this, ui("E-posta veya şifreyi kontrol et.", "Check your email or password."), Toast.LENGTH_SHORT).show();
            return;
        }
        if (firebaseAuth == null) {
            Toast.makeText(this, ui("Bulut oturumu hazır değil. İnternet bağlantını kontrol et.", "Cloud sign-in is not ready. Check your internet connection."), Toast.LENGTH_LONG).show();
            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(cleanEmail, cleanPassword)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(this, ui("Hesap oluşturulamadı: ", "Account could not be created: ") + safeMessage(task.getException()), Toast.LENGTH_LONG).show();
                        return;
                    }
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user == null) {
                        Toast.makeText(this, ui("Hesap oluşturuldu ama oturum açılamadı.", "Account was created but sign-in failed."), Toast.LENGTH_LONG).show();
                        return;
                    }
                    getSharedPreferences(PREFS, MODE_PRIVATE)
                            .edit()
                            .putString(authNameKey(cleanEmail), cleanName)
                            .putString(authProviderKey(cleanEmail), PROVIDER_EMAIL)
                            .putString(KEY_USER_NAME, cleanName)
                            .putString(KEY_USER_EMAIL, cleanEmail)
                            .putBoolean(KEY_LOGGED_IN, true)
                            .apply();
                    user.updateProfile(new UserProfileChangeRequest.Builder()
                            .setDisplayName(cleanName)
                            .build())
                            .addOnCompleteListener(profileTask -> finishFirebaseLogin(firebaseAuth.getCurrentUser()));
                    Toast.makeText(this, ui("Hesap oluşturuldu.", "Account created."), Toast.LENGTH_SHORT).show();
                });
    }

    private void loginWithEmail(EditText email, EditText password) {
        String cleanEmail = email.getText().toString().trim();
        String cleanPassword = password.getText().toString();
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String savedPassword = prefs.getString(authPasswordKey(cleanEmail), "");
        if (savedPassword.isEmpty() && cleanEmail.equalsIgnoreCase(prefs.getString(KEY_AUTH_EMAIL, ""))) {
            savedPassword = prefs.getString(KEY_AUTH_PASSWORD, "");
            if (!savedPassword.isEmpty()) {
                prefs.edit()
                        .putString(authPasswordKey(cleanEmail), savedPassword)
                        .putString(authNameKey(cleanEmail), getUserName())
                        .remove(KEY_AUTH_EMAIL)
                        .remove(KEY_AUTH_PASSWORD)
                        .apply();
            }
        }
        if (firebaseAuth == null) {
            Toast.makeText(this, ui("Bulut oturumu hazır değil. İnternet bağlantını kontrol et.", "Cloud sign-in is not ready. Check your internet connection."), Toast.LENGTH_LONG).show();
            return;
        }
        final String legacyPassword = savedPassword;
        firebaseAuth.signInWithEmailAndPassword(cleanEmail, cleanPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        prefs.edit()
                                .putString(authProviderKey(cleanEmail), PROVIDER_EMAIL)
                                .putString(KEY_USER_EMAIL, cleanEmail)
                                .putBoolean(KEY_LOGGED_IN, true)
                                .apply();
                        finishFirebaseLogin(firebaseAuth.getCurrentUser());
                        return;
                    }
                    if (!legacyPassword.isEmpty() && cleanPassword.equals(legacyPassword)) {
                        migrateLegacyEmailAccountToFirebase(cleanEmail, cleanPassword, prefs.getString(authNameKey(cleanEmail), getUserName()));
                        return;
                    }
                    Toast.makeText(this, ui("E-posta veya şifre hatalı: ", "Email or password is incorrect: ") + safeMessage(task.getException()), Toast.LENGTH_LONG).show();
                });
    }

    private void migrateLegacyEmailAccountToFirebase(String email, String password, String name) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(this, ui("Eski yerel hesap buluta taşınamadı: ", "Legacy local account could not be moved to cloud: ") + safeMessage(task.getException()), Toast.LENGTH_LONG).show();
                        return;
                    }
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    getSharedPreferences(PREFS, MODE_PRIVATE)
                            .edit()
                            .putString(KEY_USER_NAME, name)
                            .putString(KEY_USER_EMAIL, email)
                            .putString(authNameKey(email), name)
                            .putString(authProviderKey(email), PROVIDER_EMAIL)
                            .putBoolean(KEY_LOGGED_IN, true)
                            .apply();
                    if (user == null) {
                        completeLogin(name, email);
                        return;
                    }
                    user.updateProfile(new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build())
                            .addOnCompleteListener(profileTask -> finishFirebaseLogin(firebaseAuth.getCurrentUser()));
                });
    }

    private void openGoogleAccountPicker() {
        if (firebaseAuth == null || webClientId.isEmpty()) {
            Toast.makeText(this, ui("Google yapılandırması hazır değil.", "Google configuration is not ready."), Toast.LENGTH_LONG).show();
            return;
        }
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .build();
        googleClient = GoogleSignIn.getClient(this, options);
        googleClient.signOut().addOnCompleteListener(task ->
                startActivityForResult(googleClient.getSignInIntent(), REQUEST_GOOGLE_SIGN_IN)
        );
    }

    private void buildCategoriesScreen() {
        content.addView(topAppBar(ui("Kategoriler", "Categories"), false));
        content.addView(monthlyReportPanel());
        content.addView(summaryChips());
        String selectedMonthTitle = new SimpleDateFormat("MMMM yyyy", appLocale()).format(selectedReportMonth.getTime());
        content.addView(sectionTitle((isFutureMonth(selectedReportMonth) ? ui("Tahmini ", "Estimated ") : "")
                + selectedMonthTitle + ui(" Kategori Harcamaları", " Category Expenses")));

        Map<String, Double> totals = categoryTotalsForFilter();
        if (categoryFilter == 1 && !totals.isEmpty()) {
            String topCategory = null;
            double topAmount = -1;
            for (String category : totals.keySet()) {
                double amount = totals.get(category);
                if (amount > topAmount) {
                    topAmount = amount;
                    topCategory = category;
                }
            }
            Map<String, Double> reduced = new HashMap<>();
            reduced.put(topCategory, topAmount);
            totals = reduced;
        }
        List<String> categories = new ArrayList<>(totals.keySet());
        Collections.sort(categories);
        if (categories.isEmpty()) {
            content.addView(emptyText(ui("Bu ay kategoriye ayrılmış kayıt yok.", "No categorized records this month.")));
            return;
        }
        for (String category : categories) {
            int count = countItemsForCategoryFilter(category);
            content.addView(categoryCard(category, money(totals.get(category)), count + ui(" kayıt", " records")));
        }
        if (selectedCategoryDetail != null && totals.containsKey(selectedCategoryDetail)) {
            content.addView(sectionTitle(localizedCategory(selectedCategoryDetail) + ui(" Detayı", " Detail")));
            List<ExpenseItem> categoryItems = new ArrayList<>();
            for (ExpenseItem item : items) {
                if (selectedCategoryDetail.equals(item.category) && itemOccursInMonth(item, selectedReportMonth)) {
                    categoryItems.add(item);
                }
            }
            Collections.sort(categoryItems, Comparator.comparing(item -> item.name));
            for (ExpenseItem item : categoryItems) {
                Calendar due = item.dueDateForMonth(selectedReportMonth);
                String state = item.isPaidFor(due.get(Calendar.YEAR), due.get(Calendar.MONTH)) ? ui("Ödendi", "Paid") : formatDate(due);
                content.addView(itemRow(item, due, state));
            }
        }
    }

    private View monthlyReportPanel() {
        int timelineMonthCount = reportRangeMonths * 2 + 1;
        double[] totals = new double[timelineMonthCount];
        String[] labels = new String[timelineMonthCount];
        Calendar[] months = new Calendar[timelineMonthCount];
        Calendar month = Calendar.getInstance();
        month.set(Calendar.DAY_OF_MONTH, 1);
        month.add(Calendar.MONTH, -reportRangeMonths);
        SimpleDateFormat monthFormat = new SimpleDateFormat(reportRangeMonths > 6 ? "MMM yy" : "MMM", appLocale());
        for (int i = 0; i < totals.length; i++) {
            months[i] = (Calendar) month.clone();
            totals[i] = totalForMonth(month);
            labels[i] = monthFormat.format(month.getTime());
            month.add(Calendar.MONTH, 1);
        }

        double selectedTotal = totalForMonth(selectedReportMonth);
        Calendar previousMonth = (Calendar) selectedReportMonth.clone();
        previousMonth.add(Calendar.MONTH, -1);
        double previousTotal = totalForMonth(previousMonth);
        double difference = selectedTotal - previousTotal;
        double percentage = previousTotal == 0 ? 0 : (difference / previousTotal) * 100;
        int differenceColor = difference > 0 ? Color.rgb(183, 73, 31) : difference < 0 ? Color.rgb(35, 122, 63) : COLOR_MUTED;

        LinearLayout panel = formCard();
        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM);
        header.addView(text(ui("Aylık Gider Grafiği", "Monthly Expense Chart"), 18, COLOR_TEXT, Typeface.BOLD),
                new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        TextView range = text(reportRangeLabel() + "  ▾", 12, COLOR_PRIMARY, Typeface.BOLD);
        range.setPadding(dp(8), dp(6), dp(8), dp(6));
        range.setBackground(round(Color.rgb(226, 244, 246), dp(8), 0));
        range.setOnClickListener(v -> showReportRangeDialog());
        header.addView(range);
        panel.addView(header);

        TextView timelineHint = text(ui("Sola geçmiş aylar, sağa tahmini gelecek aylar • Kaydırarak inceleyin",
                        "Past months on the left, estimated future months on the right • Swipe to review"),
                12, COLOR_MUTED, Typeface.NORMAL);
        timelineHint.setPadding(0, dp(6), 0, dp(8));
        panel.addView(timelineHint);

        LinearLayout summary = new LinearLayout(this);
        summary.setOrientation(LinearLayout.VERTICAL);
        String selectedLabel = new SimpleDateFormat("MMMM yyyy", appLocale()).format(selectedReportMonth.getTime());
        summary.addView(text(selectedLabel + (isFutureMonth(selectedReportMonth) ? ui(" • Tahmin", " • Estimate") : ""),
                13, COLOR_MUTED, Typeface.BOLD));
        TextView currentTotal = text(money(selectedTotal), 27, COLOR_PRIMARY, Typeface.BOLD);
        currentTotal.setPadding(0, dp(12), 0, dp(3));
        summary.addView(currentTotal);
        String sign = difference > 0 ? "+" : difference < 0 ? "-" : "";
        String comparison = ui("Önceki aya göre ", "Compared to previous month ") + sign + money(Math.abs(difference));
        if (previousTotal > 0) {
            comparison += " (%" + decimalFormat().format(Math.abs(percentage)) + ")";
        }
        summary.addView(text(comparison, 13, differenceColor, Typeface.BOLD));
        panel.addView(summary);

        HorizontalScrollView chartScroll = new HorizontalScrollView(this);
        chartScroll.setHorizontalScrollBarEnabled(false);
        LinearLayout chart = monthlyBarViews(totals, labels, months);
        chartScroll.addView(chart, new HorizontalScrollView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp(210)));
        LinearLayout.LayoutParams chartParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(210));
        chartParams.setMargins(0, dp(10), 0, 0);
        panel.addView(chartScroll, chartParams);
        int selectedIndex = selectedMonthIndex(months);
        chartScroll.post(() -> chartScroll.scrollTo(Math.max(0, selectedIndex * dp(62) - dp(130)), 0));
        return panel;
    }

    private int selectedMonthIndex(Calendar[] months) {
        for (int i = 0; i < months.length; i++) {
            if (months[i].get(Calendar.YEAR) == selectedReportMonth.get(Calendar.YEAR)
                    && months[i].get(Calendar.MONTH) == selectedReportMonth.get(Calendar.MONTH)) {
                return i;
            }
        }
        return months.length - 1;
    }

    private LinearLayout monthlyBarViews(double[] totals, String[] labels, Calendar[] months) {
        LinearLayout chart = new LinearLayout(this);
        chart.setOrientation(LinearLayout.HORIZONTAL);
        chart.setGravity(Gravity.BOTTOM);
        chart.setPadding(0, dp(4), 0, 0);
        double max = 0;
        for (double total : totals) {
            max = Math.max(max, total);
        }
        if (max <= 0) {
            max = 1;
        }
        int selectedIndex = selectedMonthIndex(months);
        for (int i = 0; i < totals.length; i++) {
            LinearLayout column = new LinearLayout(this);
            column.setOrientation(LinearLayout.VERTICAL);
            column.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
            column.setPadding(dp(3), dp(3), dp(3), dp(3));
            if (i == selectedIndex) {
                column.setBackground(round(Color.rgb(255, 246, 230), dp(7), COLOR_ACCENT_CONTAINER));
            }

            TextView amount = text(compactMoney(totals[i]), 9, COLOR_MUTED, Typeface.BOLD);
            amount.setGravity(Gravity.CENTER);
            column.addView(amount, new LinearLayout.LayoutParams(dp(56), dp(24)));

            FrameLayout barArea = new FrameLayout(this);
            barArea.setBackground(round(COLOR_SURFACE_LOW, dp(5), 0));
            View bar = new View(this);
            int barHeight = Math.max(dp(4), (int) (dp(132) * (totals[i] / max)));
            bar.setBackground(round(i == selectedIndex ? COLOR_ACCENT_CONTAINER : COLOR_PRIMARY_CONTAINER, dp(5), 0));
            FrameLayout.LayoutParams barParams = new FrameLayout.LayoutParams(dp(30), barHeight, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            barArea.addView(bar, barParams);
            column.addView(barArea, new LinearLayout.LayoutParams(dp(50), dp(132)));

            TextView label = text(labels[i], 10, i == selectedIndex ? COLOR_ACCENT : COLOR_MUTED, Typeface.BOLD);
            label.setGravity(Gravity.CENTER);
            column.addView(label, new LinearLayout.LayoutParams(dp(58), dp(32)));

            Calendar selectedMonth = (Calendar) months[i].clone();
            column.setOnClickListener(v -> {
                selectedReportMonth.setTimeInMillis(selectedMonth.getTimeInMillis());
                selectedReportMonth.set(Calendar.DAY_OF_MONTH, 1);
                showScreen(SCREEN_CATEGORIES);
            });
            LinearLayout.LayoutParams columnParams = new LinearLayout.LayoutParams(dp(62), dp(196));
            columnParams.setMargins(0, 0, dp(2), 0);
            chart.addView(column, columnParams);
        }
        return chart;
    }

    private String reportRangeLabel() {
        if (reportRangeMonths == 12) return ui("± 1 yıl", "± 1 year");
        if (reportRangeMonths == 36) return ui("± 3 yıl", "± 3 years");
        return ui("± 6 ay", "± 6 months");
    }

    private void showReportRangeDialog() {
        String[] labels = {ui("Geçmiş/Gelecek 6 ay", "Past/Future 6 months"), ui("Geçmiş/Gelecek 1 yıl", "Past/Future 1 year"), ui("Geçmiş/Gelecek 3 yıl", "Past/Future 3 years")};
        int[] months = {6, 12, 36};
        int checked = reportRangeMonths == 12 ? 1 : reportRangeMonths == 36 ? 2 : 0;
        new AlertDialog.Builder(this)
                .setTitle(ui("Grafik Zaman Aralığı", "Chart Time Range"))
                .setSingleChoiceItems(labels, checked, (dialog, which) -> {
                    reportRangeMonths = months[which];
                    selectedReportMonth.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
                    selectedReportMonth.set(Calendar.DAY_OF_MONTH, 1);
                    dialog.dismiss();
                    showScreen(SCREEN_CATEGORIES);
                })
                .setNegativeButton(ui("Vazgeç", "Cancel"), null)
                .show();
    }

    private boolean isFutureMonth(Calendar month) {
        Calendar now = Calendar.getInstance();
        return month.get(Calendar.YEAR) * 12 + month.get(Calendar.MONTH)
                > now.get(Calendar.YEAR) * 12 + now.get(Calendar.MONTH);
    }

    private double totalForMonth(Calendar target) {
        double total = 0;
        for (ExpenseItem item : items) {
            if (itemOccursInMonth(item, target)) {
                total += item.amountForMonth(target.get(Calendar.YEAR), target.get(Calendar.MONTH));
            }
        }
        return total;
    }

    private boolean itemOccursInMonth(ExpenseItem item, Calendar target) {
        int targetIndex = target.get(Calendar.YEAR) * 12 + target.get(Calendar.MONTH);
        int itemIndex = item.year * 12 + item.month;
        if (targetIndex < itemIndex || (!item.monthly && targetIndex != itemIndex)) {
            return false;
        }
        return !item.hasEndDate || targetIndex <= item.endYear * 12 + item.endMonth;
    }

    private String compactMoney(double amount) {
        if (amount >= 1000) {
            return currencySymbol() + String.format(appLocale(), "%.1f B", amount / 1000);
        }
        return currencySymbol() + String.format(appLocale(), "%.0f", amount);
    }

    private void buildCalendarScreen() {
        Calendar now = (Calendar) displayedCalendar.clone();
        selectedCalendarDay = Math.max(1, Math.min(selectedCalendarDay, now.getActualMaximum(Calendar.DAY_OF_MONTH)));
        content.addView(calendarTopBar());
        content.addView(calendarCard(now));
        List<ExpenseItem> selectedDayItems = itemsForDay(selectedCalendarDay, now);
        double selectedTotal = 0;
        for (ExpenseItem item : selectedDayItems) {
            selectedTotal += item.amountForMonth(now.get(Calendar.YEAR), now.get(Calendar.MONTH));
        }
        Calendar selectedDate = (Calendar) now.clone();
        selectedDate.set(Calendar.DAY_OF_MONTH, selectedCalendarDay);
        LinearLayout dayHeader = new LinearLayout(this);
        dayHeader.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM);
        dayHeader.setPadding(0, 0, 0, dp(12));
        dayHeader.addView(text(new SimpleDateFormat("d MMMM, EEEE", appLocale()).format(selectedDate.getTime()), 18, COLOR_TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        dayHeader.addView(text(ui("Toplam: ", "Total: ") + money(selectedTotal), 12, COLOR_PRIMARY, Typeface.BOLD));
        content.addView(dayHeader);

        if (!selectedDayItems.isEmpty()) {
            Collections.sort(selectedDayItems, Comparator.comparing(item -> item.name));
            for (ExpenseItem item : selectedDayItems) {
                content.addView(calendarPaymentRow(item, item.dueDateForMonth(selectedDate)));
            }
        } else {
            content.addView(emptyText(ui("Seçili gün için ödeme görünmüyor.", "No payment for the selected day.")));
        }

        content.addView(sectionHeader(ui("Bu Ayın Tüm Ödemeleri", "All Payments This Month"), ui("Bu Ay", "This Month")));

        List<ExpenseItem> monthItems = itemsForMonth(now);
        if (monthItems.isEmpty()) {
            content.addView(emptyText(ui("Bu ay takvimde görünen ödeme yok.", "No payments visible this month.")));
            return;
        }
        Collections.sort(monthItems, Comparator.comparingLong(item -> item.dueDateForMonth(now).getTimeInMillis()));
        int lastDay = -1;
        for (ExpenseItem item : monthItems) {
            Calendar due = item.dueDateForMonth(now);
            int day = due.get(Calendar.DAY_OF_MONTH);
            if (day != lastDay) {
                content.addView(dayGroupHeader(due, totalForDay(day, now)));
                lastDay = day;
            }
            content.addView(calendarPaymentRow(item, due));
        }
    }

    private void buildSettingsScreen() {
        content.addView(topAppBar(ui("Ayarlar", "Settings"), false));
        content.addView(profileCard());
        content.addView(settingsGroup(ui("Hesap", "Account"),
                settingRow(ui("Kişisel Bilgiler", "Personal Info"), getUserName(), "👤", v -> showPersonalInfoDialog()),
                settingRow(ui("Bildirim Ayarları", "Notifications"), notificationsEnabled() ? ui("Aktif", "On") : ui("Kapalı", "Off"), "🔔", v -> showNotificationDialog())
        ));
        content.addView(settingsGroup(ui("Tercihler", "Preferences"),
                settingRow(ui("Para Birimi", "Currency"), getCurrency(), "₺", v -> showCurrencyDialog()),
                settingRow(ui("Ülke", "Country"), currentCountryLabel(), "⌂", v -> showCountryLanguageSetupDialog()),
                settingRow(ui("Tema", "Theme"), getThemeDisplayName(), "◐", v -> showThemeDialog()),
                settingRow(ui("Dil", "Language"), getLanguageName(), "🌐", v -> showLanguageDialog())
        ));
        content.addView(settingsGroup(ui("Bilgi", "Info"),
                settingRow(ui("Güvenlik", "Security"), ui("Yerel kayıt", "Local records"), "🔒", v -> showSecurityDialog()),
                settingRow(ui("Hakkımızda", "About"), "1.0.0", "ⓘ", v -> showAboutDialog())
        ));
        content.addView(settingsGroup(ui("Oturum", "Session"),
                settingRow(ui("Çıkış Yap", "Sign Out"), getUserEmail(), "↪", v -> showLogoutDialog())
        ));
    }

    private void openRecordScreen(ExpenseItem item) {
        editingItem = item;
        returnScreen = currentScreen == SCREEN_RECORD ? SCREEN_HOME : currentScreen;
        showScreen(SCREEN_RECORD);
    }

    private void buildRecordScreen() {
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(0, dp(8), 0, dp(24));
        TextView back = text("‹", 38, COLOR_TEXT, Typeface.NORMAL);
        back.setGravity(Gravity.CENTER);
        back.setOnClickListener(v -> showScreen(returnScreen));
        header.addView(back, new LinearLayout.LayoutParams(dp(42), dp(52)));
        TextView title = text(editingItem == null ? ui("Yeni Kayıt", "New Record") : ui("Kaydı Düzenle", "Edit Record"), 26, COLOR_PRIMARY, Typeface.BOLD);
        header.addView(title, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        View profile = profileAvatar(dp(TOP_AVATAR_SIZE_DP));
        profile.setOnClickListener(v -> showScreen(SCREEN_SETTINGS));
        header.addView(profile, new LinearLayout.LayoutParams(dp(TOP_AVATAR_SIZE_DP), dp(TOP_AVATAR_SIZE_DP)));
        content.addView(header);

        EditText name = field(ui("Örn: Netflix Premium", "Ex: Netflix Premium"));
        EditText amount = field("0,00");
        amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if (editingItem != null) {
            name.setText(editingItem.name);
            amount.setText(String.valueOf(editingItem.amount));
        }

        Spinner category = new Spinner(this);
        category.setAdapter(categoryAdapter());
        category.setSelection(categoryIndex(editingItem == null ? "Abonelik" : editingItem.category));
        category.setBackground(round(
                categoryTint(editingItem == null ? "Abonelik" : editingItem.category),
                dp(12),
                categoryTextColor(editingItem == null ? "Abonelik" : editingItem.category)
        ));
        Calendar selected = Calendar.getInstance();
        if (editingItem != null) {
            selected.set(editingItem.year, editingItem.month, editingItem.day);
        }
        TextView date = fieldLikeText(ui("Ödeme Tarihi: ", "Payment Date: ") + formatDate(selected));
        date.setOnClickListener(v -> new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selected.set(year, month, dayOfMonth);
                    date.setText(ui("Ödeme Tarihi: ", "Payment Date: ") + formatDate(selected));
                },
                selected.get(Calendar.YEAR),
                selected.get(Calendar.MONTH),
                selected.get(Calendar.DAY_OF_MONTH)
        ).show());

        LinearLayout categoryCard = formCard();
        LinearLayout categoryHeader = new LinearLayout(this);
        categoryHeader.setGravity(Gravity.CENTER_VERTICAL);
        categoryHeader.addView(formLabel(ui("KATEGORİ SEÇİMİ", "CATEGORY")), new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        categoryHeader.addView(text(ui("Tümünü Gör", "See All"), 14, COLOR_PRIMARY, Typeface.BOLD));
        categoryCard.addView(categoryHeader);
        categoryCard.addView(category, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(58)));
        categoryCard.addView(spacer(dp(16)));
        categoryCard.addView(date, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(56)));
        content.addView(categoryCard);

        View popularServices = popularServicesCard(name, amount);
        popularServices.setVisibility((editingItem == null || "Abonelik".equals(editingItem.category)) ? View.VISIBLE : View.GONE);
        content.addView(popularServices);

        LinearLayout infoCard = formCard();
        infoCard.addView(formLabel(ui("Kayıt Adı", "Record Name")));
        infoCard.addView(name, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(56)));
        infoCard.addView(spacer(dp(18)));
        infoCard.addView(formLabel(ui("Tutar", "Amount")));
        infoCard.addView(amount, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(56)));
        content.addView(infoCard);

        CheckBox monthly = styledCheck(ui("Her ay tekrar eder", "Repeats monthly"), ui("Otomatik kayıt oluşturulur", "Creates automatic monthly records"));
        monthly.setChecked(editingItem == null || editingItem.monthly);
        TextView smartSuggestion = text(ui("Akıllı öneri: hizmet adını yazınca kategori ve tekrar seçimi otomatik önerilir.", "Smart suggestion: type a service name to auto-suggest category and repeat."),
                13, COLOR_MUTED, Typeface.NORMAL);
        smartSuggestion.setPadding(dp(4), 0, dp(4), dp(12));
        if (editingItem == null) {
            name.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    applySmartRecordSuggestion(s.toString(), category, monthly, smartSuggestion);
                }
                @Override public void afterTextChanged(Editable s) {}
            });
        }
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private boolean firstSelection = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = CATEGORIES[position];
                category.setBackground(round(categoryTint(selectedCategory), dp(12), categoryTextColor(selectedCategory)));
                if (editingItem == null || !firstSelection) {
                    monthly.setChecked(isRecurringCategory(selectedCategory));
                }
                if ("Abonelik".equals(selectedCategory)) {
                    popularServices.setVisibility(View.VISIBLE);
                } else {
                    popularServices.setVisibility(View.GONE);
                    if (editingItem == null) {
                        name.setText(localizedCategory(selectedCategory));
                        name.setSelection(name.getText().length());
                    }
                }
                firstSelection = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        CheckBox reminder = styledCheck(ui("Hatırlatma aç", "Enable reminder"), ui("Seçtiğiniz aralıkta ödeme öncesi bildir", "Notify before payment at your chosen interval"));
        reminder.setChecked(editingItem == null || editingItem.reminderEnabled);
        String[] reminderOptions = {ui("Günlük", "Daily"), ui("Haftalık", "Weekly"), ui("Aylık", "Monthly"), ui("Yıllık", "Yearly")};
        String[] selectedReminderFrequency = {editingItem == null ? ui("Günlük", "Daily") : localizedReminderFrequency(editingItem.reminderFrequency)};
        TextView reminderFrequency = fieldLikeText(ui("Bildirim sıklığı: ", "Reminder frequency: ") + selectedReminderFrequency[0] + "  ▾");
        reminderFrequency.setOnClickListener(v -> showReminderFrequencyDialog(
                reminderOptions, selectedReminderFrequency, reminderFrequency));

        Calendar endSelected = Calendar.getInstance();
        boolean[] hasEndDate = {editingItem != null && editingItem.hasEndDate};
        if (hasEndDate[0]) {
            endSelected.set(editingItem.endYear, editingItem.endMonth, editingItem.endDay);
        } else {
            endSelected.setTimeInMillis(selected.getTimeInMillis());
            endSelected.add(Calendar.YEAR, 1);
        }
        TextView endDate = fieldLikeText(endDateLabel(hasEndDate[0], endSelected));
        endDate.setOnClickListener(v -> showEndDateDialog(endDate, endSelected, hasEndDate));
        LinearLayout automation = formCard();
        automation.addView(smartSuggestion);
        automation.addView(monthly);
        automation.addView(divider());
        automation.addView(reminder);
        automation.addView(formLabel(ui("BİLDİRİM SIKLIĞI", "REMINDER FREQUENCY")));
        automation.addView(reminderFrequency, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(56)));
        automation.addView(spacer(dp(14)));
        automation.addView(formLabel(ui("ABONELİK BİTİŞ TARİHİ", "SUBSCRIPTION END DATE")));
        automation.addView(endDate, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(56)));
        content.addView(automation);

        Button save = secondaryButton("▣  " + ui("Kaydet", "Save"));
        save.setTextColor(Color.rgb(44, 23, 0));
        save.setOnClickListener(v -> saveRecordFromForm(name, amount, category, monthly, selected,
                reminder.isChecked(), selectedReminderFrequency[0], hasEndDate[0], endSelected));
        LinearLayout.LayoutParams saveParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(58)
        );
        saveParams.setMargins(0, dp(18), 0, dp(28));
        content.addView(save, saveParams);

        if (editingItem != null) {
            ExpenseItem itemToDelete = editingItem;
            Button delete = outlineButton(ui("Sil", "Delete"));
            delete.setTextColor(Color.rgb(147, 45, 35));
            delete.setOnClickListener(v -> confirmDeleteFromEdit(itemToDelete));
            LinearLayout.LayoutParams deleteParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, dp(56));
            deleteParams.setMargins(0, dp(12), 0, 0);
            content.addView(delete, deleteParams);
        }

        if (editingItem != null && isCancellableService(editingItem)) {
            Button cancelSubscription = outlineButton("Aboneliği İptal Et");
            cancelSubscription.setTextColor(Color.rgb(147, 45, 35));
            cancelSubscription.setOnClickListener(v -> openCancellationPage(editingItem));
            LinearLayout.LayoutParams cancelParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(54));
            cancelParams.setMargins(0, dp(14), 0, 0);
            content.addView(cancelSubscription, cancelParams);
        }
    }

    private View popularServicesCard(EditText name, EditText amount) {
        LinearLayout card = formCard();
        LinearLayout titleRow = new LinearLayout(this);
        titleRow.setGravity(Gravity.CENTER_VERTICAL);
        titleRow.addView(text(ui("POPÜLER HİZMETLER", "POPULAR SERVICES"), 15, COLOR_TEXT, Typeface.BOLD),
                new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        titleRow.addView(text(currentCountryLabel(), 13, COLOR_PRIMARY, Typeface.BOLD));
        card.addView(titleRow);

        TextView description = text(ui("Ülkene göre popüler hizmetleri seç, kayıt bilgileri otomatik dolsun.", "Pick popular services for your region and auto-fill the record."), 13, COLOR_MUTED, Typeface.NORMAL);
        description.setPadding(0, dp(6), 0, dp(12));
        card.addView(description);

        String[] services = popularServicesForCountry();
        HorizontalScrollView scroll = new HorizontalScrollView(this);
        scroll.setHorizontalScrollBarEnabled(false);
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        for (String service : services) {
            TextView chip = text(service, 13, COLOR_PRIMARY, Typeface.BOLD);
            chip.setGravity(Gravity.CENTER);
            chip.setPadding(dp(16), 0, dp(16), 0);
            chip.setBackground(round(Color.rgb(226, 244, 246), dp(20), COLOR_LINE));
            chip.setOnClickListener(v -> showServicePlans(service, name, amount));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, dp(44));
            params.setMargins(0, 0, dp(10), 0);
            row.addView(chip, params);
        }
        scroll.addView(row);
        card.addView(scroll);
        return card;
    }

    private void showServicePlans(String service, EditText name, EditText amount) {
        String[] plans;
        double[] prices;
        boolean turkey = "TR".equals(currentCountryCode());
        if (turkey) {
            if ("Netflix".equals(service)) {
                plans = new String[]{"Temel - 189,99 TL/ay", "Standart - 289,99 TL/ay", "Özel - 379,99 TL/ay"};
                prices = new double[]{189.99, 289.99, 379.99};
            } else if ("Amazon Prime".equals(service)) {
                plans = new String[]{"Prime Aylık - 69,90 TL/ay"};
                prices = new double[]{69.90};
            } else if ("Spotify".equals(service)) {
                plans = new String[]{"Bireysel - 99 TL/ay", "Öğrenci - 55 TL/ay", "Duo - 135 TL/ay", "Aile - 165 TL/ay"};
                prices = new double[]{99, 55, 135, 165};
            } else if ("YouTube Premium".equals(service)) {
                plans = new String[]{"Premium Lite - 79,99 TL/ay", "Bireysel - 119,99 TL/ay", "Öğrenci - 79,99 TL/ay", "Aile - 239,99 TL/ay"};
                prices = new double[]{79.99, 119.99, 79.99, 239.99};
            } else if ("Disney+".equals(service)) {
                plans = new String[]{"Reklamlı - 249,90 TL/ay", "Reklamsız - 449,90 TL/ay"};
                prices = new double[]{249.90, 449.90};
            } else if ("Exxen".equals(service)) {
                plans = new String[]{"7 gün deneme - 1 TL", "Aylık paket - güncel fiyatı gir"};
                prices = new double[]{1, -1};
            } else if ("GAİN".equals(service)) {
                plans = new String[]{"İlk 3 ay kampanyası - 129 TL/ay", "Aylık paket - güncel fiyatı gir"};
                prices = new double[]{129, -1};
            } else if ("TOD".equals(service)) {
                plans = new String[]{"Eğlence paketi - güncel fiyatı gir", "Spor paketi - güncel fiyatı gir"};
                prices = new double[]{-1, -1};
            } else if ("beIN CONNECT".equals(service)) {
                plans = new String[]{"Yıldız Dolu - güncel fiyatı gir", "Spor paketi - güncel fiyatı gir"};
                prices = new double[]{-1, -1};
            } else {
                plans = new String[]{localizedPlanLabel("Monthly plan"), localizedPlanLabel("Annual plan")};
                prices = new double[]{-1, -1};
            }
        } else if ("Netflix".equals(service)) {
            plans = new String[]{localizedPlanLabel("Standard with ads"), localizedPlanLabel("Standard"), localizedPlanLabel("Premium")};
            prices = new double[]{-1, -1, -1};
        } else if ("Amazon Prime".equals(service)) {
            plans = new String[]{localizedPlanLabel("Prime Monthly")};
            prices = new double[]{-1};
        } else if ("Spotify".equals(service)) {
            plans = new String[]{localizedPlanLabel("Individual"), localizedPlanLabel("Student"), localizedPlanLabel("Duo"), localizedPlanLabel("Family")};
            prices = new double[]{-1, -1, -1, -1};
        } else if ("YouTube Premium".equals(service)) {
            plans = new String[]{localizedPlanLabel("Individual"), localizedPlanLabel("Student"), localizedPlanLabel("Family")};
            prices = new double[]{-1, -1, -1};
        } else if ("Disney+".equals(service)) {
            plans = new String[]{localizedPlanLabel("With ads"), localizedPlanLabel("Ad-free")};
            prices = new double[]{-1, -1};
        } else if ("TOD".equals(service) || "beIN CONNECT".equals(service)) {
            plans = new String[]{localizedPlanLabel("Entertainment plan"), localizedPlanLabel("Sports plan")};
            prices = new double[]{-1, -1};
        } else {
            plans = new String[]{localizedPlanLabel("Monthly plan"), localizedPlanLabel("Annual plan")};
            prices = new double[]{-1, -1};
        }
        new AlertDialog.Builder(this)
                .setTitle(service + " " + ui("paketleri", "plans"))
                .setSingleChoiceItems(plans, -1, (dialog, which) -> {
                    String planName = plans[which].split(" - ")[0];
                    name.setText(service + " " + planName);
                    if (prices[which] > 0) {
                        amount.setText(String.format(Locale.US, "%.2f", prices[which]));
                    } else {
                        amount.setText("");
                        amount.requestFocus();
                    }
                    dialog.dismiss();
                    String message = prices[which] > 0
                            ? ui("Paket seçildi. Tarihi kontrol edip kaydedebilirsin.", "Plan selected. Check the date and save.")
                            : ui("Paket seçildi. Hizmette görünen güncel tutarı gir.", "Plan selected. Enter the current price shown by the service.");
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(ui("Vazgeç", "Cancel"), null)
                .show();
    }

    private String localizedPlanLabel(String plan) {
        return uiPlan(plan) + " - " + ui("güncel fiyatı gir", "enter current price");
    }

    private String uiPlan(String plan) {
        String language = getLanguageCode();
        if ("tr".equals(language)) {
            if ("Standard with ads".equals(plan)) return "Reklamlı standart";
            if ("Standard".equals(plan)) return "Standart";
            if ("Premium".equals(plan)) return "Premium";
            if ("Prime Monthly".equals(plan)) return "Prime Aylık";
            if ("Individual".equals(plan)) return "Bireysel";
            if ("Student".equals(plan)) return "Öğrenci";
            if ("Duo".equals(plan)) return "Duo";
            if ("Family".equals(plan)) return "Aile";
            if ("With ads".equals(plan)) return "Reklamlı";
            if ("Ad-free".equals(plan)) return "Reklamsız";
            if ("Entertainment plan".equals(plan)) return "Eğlence paketi";
            if ("Sports plan".equals(plan)) return "Spor paketi";
            if ("Annual plan".equals(plan)) return "Yıllık paket";
            return "Aylık paket";
        }
        if ("es".equals(language)) {
            if ("Standard with ads".equals(plan)) return "Estándar con anuncios";
            if ("Standard".equals(plan)) return "Estándar";
            if ("Premium".equals(plan)) return "Premium";
            if ("Prime Monthly".equals(plan)) return "Prime mensual";
            if ("Individual".equals(plan)) return "Individual";
            if ("Student".equals(plan)) return "Estudiante";
            if ("Duo".equals(plan)) return "Duo";
            if ("Family".equals(plan)) return "Familiar";
            if ("With ads".equals(plan)) return "Con anuncios";
            if ("Ad-free".equals(plan)) return "Sin anuncios";
            if ("Entertainment plan".equals(plan)) return "Plan de entretenimiento";
            if ("Sports plan".equals(plan)) return "Plan deportivo";
            if ("Annual plan".equals(plan)) return "Plan anual";
            return "Plan mensual";
        }
        if ("de".equals(language)) {
            if ("Standard with ads".equals(plan)) return "Standard mit Werbung";
            if ("Student".equals(plan)) return "Student";
            if ("Family".equals(plan)) return "Familie";
            if ("With ads".equals(plan)) return "Mit Werbung";
            if ("Ad-free".equals(plan)) return "Werbefrei";
            if ("Entertainment plan".equals(plan)) return "Entertainment-Tarif";
            if ("Sports plan".equals(plan)) return "Sport-Tarif";
            if ("Annual plan".equals(plan)) return "Jahrestarif";
            if ("Monthly plan".equals(plan)) return "Monatstarif";
            return plan;
        }
        if ("fr".equals(language)) {
            if ("Standard with ads".equals(plan)) return "Standard avec pubs";
            if ("Student".equals(plan)) return "Étudiant";
            if ("Family".equals(plan)) return "Famille";
            if ("With ads".equals(plan)) return "Avec pubs";
            if ("Ad-free".equals(plan)) return "Sans pubs";
            if ("Entertainment plan".equals(plan)) return "Formule divertissement";
            if ("Sports plan".equals(plan)) return "Formule sport";
            if ("Annual plan".equals(plan)) return "Formule annuelle";
            if ("Monthly plan".equals(plan)) return "Formule mensuelle";
            return plan;
        }
        if ("pt".equals(language)) {
            if ("Standard with ads".equals(plan)) return "Padrão com anúncios";
            if ("Standard".equals(plan)) return "Padrão";
            if ("Student".equals(plan)) return "Estudante";
            if ("Family".equals(plan)) return "Família";
            if ("With ads".equals(plan)) return "Com anúncios";
            if ("Ad-free".equals(plan)) return "Sem anúncios";
            if ("Entertainment plan".equals(plan)) return "Plano de entretenimento";
            if ("Sports plan".equals(plan)) return "Plano esportivo";
            if ("Annual plan".equals(plan)) return "Plano anual";
            if ("Monthly plan".equals(plan)) return "Plano mensal";
            return plan;
        }
        return plan;
    }
    private String[] popularServicesForCountry() {
        String country = currentCountryCode();
        if ("US".equals(country)) {
            return new String[]{"Netflix", "Hulu", "Disney+", "Max", "Amazon Prime", "Spotify", "YouTube Premium", "Apple TV+", "Paramount+", "Peacock", "Crunchyroll"};
        }
        if ("GB".equals(country) || "UK".equals(country)) {
            return new String[]{"Netflix", "Amazon Prime", "Disney+", "Apple TV+", "Spotify", "YouTube Premium", "NOW", "Paramount+", "BritBox", "DAZN"};
        }
        if ("DE".equals(country)) {
            return new String[]{"Netflix", "Amazon Prime", "Disney+", "Spotify", "YouTube Premium", "Apple TV+", "WOW", "RTL+", "DAZN", "Paramount+"};
        }
        if ("FR".equals(country)) {
            return new String[]{"Netflix", "Amazon Prime", "Disney+", "Spotify", "YouTube Premium", "Apple TV+", "Canal+", "Paramount+", "DAZN"};
        }
        if ("ES".equals(country)) {
            return new String[]{"Netflix", "Amazon Prime", "Disney+", "Spotify", "YouTube Premium", "Apple TV+", "Max", "DAZN", "Paramount+"};
        }
        if ("PT".equals(country)) {
            return new String[]{"Netflix", "Amazon Prime", "Disney+", "Spotify", "YouTube Premium", "Apple TV+", "Max", "DAZN", "SkyShowtime"};
        }
        if ("BR".equals(country)) {
            return new String[]{"Netflix", "Amazon Prime", "Disney+", "Spotify", "YouTube Premium", "Apple TV+", "Max", "Paramount+", "Globoplay"};
        }
        if ("CA".equals(country)) {
            return new String[]{"Netflix", "Amazon Prime", "Disney+", "Spotify", "YouTube Premium", "Apple TV+", "Crave", "Paramount+", "Crunchyroll"};
        }
        if ("AU".equals(country)) {
            return new String[]{"Netflix", "Amazon Prime", "Disney+", "Spotify", "YouTube Premium", "Apple TV+", "Stan", "Paramount+", "Binge"};
        }
        if ("IN".equals(country)) {
            return new String[]{"Netflix", "Amazon Prime", "Disney+", "Spotify", "YouTube Premium", "Apple TV+", "JioHotstar", "SonyLIV", "ZEE5"};
        }
        if ("JP".equals(country)) {
            return new String[]{"Netflix", "Amazon Prime", "Disney+", "Spotify", "YouTube Premium", "Apple TV+", "U-NEXT", "Hulu", "DAZN"};
        }
        return new String[]{
                "Netflix", "Amazon Prime", "Spotify", "YouTube Premium", "Disney+",
                "HBO Max", "Exxen", "GAİN", "TOD", "beIN CONNECT", "tabii"
        };
    }

    private void applySmartRecordSuggestion(String value, Spinner category, CheckBox monthly, TextView suggestion) {
        String normalized = normalizeServiceText(value);
        if (normalized.isEmpty()) {
            suggestion.setText("Akıllı öneri: hizmet adını yazınca kategori ve tekrar seçimi otomatik önerilir.");
            return;
        }
        String suggestedCategory = null;
        if (containsAny(normalized, "netflix", "spotify", "youtube", "prime", "amazon", "disney", "exxen", "gain", "gaın", "tod", "bein", "hbo", "max", "tabii", "hulu", "paramount", "peacock", "apple tv", "crunchyroll", "britbox", "dazn", "canal", "now")) {
            suggestedCategory = "Abonelik";
        } else if (containsAny(normalized, "turkcell", "vodafone", "turk telekom", "telekom", "telefon", "hat")) {
            suggestedCategory = "Telefon";
        } else if (containsAny(normalized, "internet", "fiber", "superonline", "kablonet", "millenicom", "netspeed")) {
            suggestedCategory = "İnternet";
        } else if (containsAny(normalized, "elektrik", "ck", "bedaş", "ayedas", "enerjisa")) {
            suggestedCategory = "Elektrik";
        } else if (containsAny(normalized, "su", "iski", "aski")) {
            suggestedCategory = "Su";
        } else if (containsAny(normalized, "dogalgaz", "doğalgaz", "igdas", "igdaş", "gaz")) {
            suggestedCategory = "Doğalgaz";
        } else if (containsAny(normalized, "kira", "ev")) {
            suggestedCategory = "Kira";
        }
        if (suggestedCategory == null) {
            suggestion.setText("Akıllı öneri: bu kayıt için özel öneri bulunamadı.");
            return;
        }
        int index = categoryIndex(suggestedCategory);
        if (index >= 0 && category.getSelectedItemPosition() != index) {
            category.setSelection(index);
        }
        monthly.setChecked(isRecurringCategory(suggestedCategory));
        suggestion.setText("Akıllı öneri: " + suggestedCategory + " kategorisi ve aylık tekrar önerildi.");
    }

    private boolean containsAny(String text, String... needles) {
        for (String needle : needles) {
            if (text.contains(needle)) {
                return true;
            }
        }
        return false;
    }

    private String normalizeServiceText(String value) {
        return value == null ? "" : value.trim().toLowerCase(new Locale("tr", "TR"));
    }

    private void buildCampaignsScreen() {
        content.addView(topAppBar(ui("Kampanyalar", "Campaigns"), false));
        TextView intro = text(ui("Fırsatlar & Kampanyalar", "Deals & Campaigns"), 17, COLOR_TEXT, Typeface.BOLD);
        intro.setPadding(0, dp(8), 0, dp(12));
        content.addView(intro);

        LinearLayout campaignList = new LinearLayout(this);
        campaignList.setOrientation(LinearLayout.VERTICAL);
        String cached = getSharedPreferences(PREFS, MODE_PRIVATE).getString(KEY_CAMPAIGN_CACHE, "");
        if (!renderCampaignFeed(campaignList, cached, false)) {
            renderFallbackCampaignsForCountry(campaignList, false);
        }
        content.addView(campaignList);
        loadRemoteCampaigns(campaignList, false);

        TextView note = text(ui("Kampanya koşulları ve fiyatlar değişebilir. Kartlara dokunduğunda güncel resmî kampanya sayfası açılır.",
                "Campaign terms and prices may change. Tap a card to open the current official campaign page."), 13, COLOR_MUTED, Typeface.NORMAL);
        note.setPadding(dp(4), dp(6), dp(4), dp(20));
        content.addView(note);
    }

    private void renderFallbackCampaignsForCountry(LinearLayout target, boolean compact) {
        if (!"TR".equals(currentCountryCode())) {
            target.removeAllViews();
            return;
        }
        target.addView(campaignCard(ui("İş Bankası kartlarına 6–12 ay Amazon Prime", "6–12 months Amazon Prime with İş Bank cards"), ui("6 veya 12 ay ücretsiz", "6 or 12 months free"),
                ui("Amazon Prime • Kart türüne göre üyelik süresi değişir.", "Amazon Prime • Membership duration varies by card type."), ui("30 Nisan 2027'ye kadar", "Until April 30, 2027"),
                "https://www.maximum.com.tr/kampanyalar/12-aya-varan-amazon-prime-uyeligi", compact));
        target.addView(campaignCard(ui("Bankkart ile beIN CONNECT Yıldız Dolu", "beIN CONNECT with Bankkart"), ui("359 TL / ay + 1.000 TL Bankkart Lira", "359 TRY/month + 1,000 TRY Bankkart Lira"),
                ui("beIN CONNECT, TOD • Yeni üyeliklere özel.", "beIN CONNECT, TOD • For new memberships."), ui("3 Ağustos 2026'ya kadar", "Until August 3, 2026"),
                "https://www.bankkart.com.tr/kampanyalar/diger-kampanyalar/digiturk-yildiz-dolu-paket-aylik-359-tl-ve-1000-tl-bankkart-lira", compact));
        target.addView(campaignCard(ui("Bankkart Genç'e özel beIN CONNECT", "beIN CONNECT for Bankkart Genç"), ui("99 TL / ay + 500 TL Bankkart Lira", "99 TRY/month + 500 TRY Bankkart Lira"),
                ui("beIN CONNECT, TOD • Öğrencilere özel.", "beIN CONNECT, TOD • For students."), ui("4 Temmuz 2026'ya kadar", "Until July 4, 2026"),
                "https://www.bankkart.com.tr/kampanyalar/diger-kampanyalar/digiturk-yildiz-dolu-bankkart-genclilere-ayda-99-tl-ve-500-tl-bankkart-lira", compact));
        target.addView(campaignCard(ui("Amazon Prime ilk 3 ay Vodafone'dan", "First 3 months of Amazon Prime from Vodafone"), ui("İlk 3 ay hediye", "First 3 months free"),
                ui("Amazon Prime • Uygun Vodafone müşterilerine özel.", "Amazon Prime • For eligible Vodafone customers."), ui("Resmî kampanya", "Official campaign"),
                "https://www.vodafone.com.tr/kampanyalar/amazon-prime-uyeliginiz-ilk-3-ay-vodafonedan", compact));
        String turkcellUrl = "https://www.turkcell.com.tr/servisler/turkcell-one";
        target.addView(campaignCard("Turkcell One Star", "500 TL / ay", ui("Netflix, Amazon Prime, YouTube Premium, HBO Max ve TV+.", "Netflix, Amazon Prime, YouTube Premium, HBO Max and TV+."), ui("Resmî kampanya", "Official campaign"), turkcellUrl, compact));
        target.addView(campaignCard("Turkcell One Plus", "600 TL / ay", ui("Netflix, Amazon Prime, YouTube Premium, HBO Max, TV+ ve fizy.", "Netflix, Amazon Prime, YouTube Premium, HBO Max, TV+ and fizy."), ui("Resmî kampanya", "Official campaign"), turkcellUrl, compact));
        target.addView(campaignCard("Turkcell One Premium", "690 TL / ay", ui("Netflix, Amazon Prime, YouTube Premium, HBO Max, TV+ ve fizy.", "Netflix, Amazon Prime, YouTube Premium, HBO Max, TV+ and fizy."), ui("Resmî kampanya", "Official campaign"), turkcellUrl, compact));
    }

    private boolean renderCampaignFeed(LinearLayout target, String json, boolean compact) {
        if (json == null || json.trim().isEmpty()) {
            return false;
        }
        try {
            JSONObject root = new JSONObject(json);
            JSONArray campaigns = root.getJSONArray("campaigns");
            if (campaigns.length() == 0) {
                return false;
            }
            target.removeAllViews();
            List<JSONObject> ordered = new ArrayList<>();
            for (int i = 0; i < campaigns.length(); i++) {
                ordered.add(campaigns.getJSONObject(i));
            }
            Collections.sort(ordered, (a, b) -> campaignMatchScore(b) - campaignMatchScore(a));
            int added = 0;
            for (int i = 0; i < ordered.size(); i++) {
                JSONObject item = ordered.get(i);
                String status = item.optString("status", "active");
                boolean expired = "expired".equals(status);
                if (expired) {
                    continue;
                }
                if (!campaignMatchesCountry(item)) {
                    continue;
                }
                JSONArray serviceArray = item.optJSONArray("services");
                List<String> services = new ArrayList<>();
                if (serviceArray != null) {
                    for (int j = 0; j < serviceArray.length(); j++) {
                        services.add(serviceArray.optString(j));
                    }
                }
                String description = item.optString("description", "");
                if (!services.isEmpty()) {
                    description = TextUtils.join(", ", services) + ". " + description;
                }
                String badge = item.optString("badge", ui("Resmî kaynak", "Official source"));
                if (expired) {
                    badge = ui("Süresi doldu • ", "Expired • ") + badge;
                }
                int score = campaignMatchScore(item);
                if (score > 0) {
                    badge = ui("Sana uygun • ", "For you • ") + badge;
                }
                target.addView(campaignCard(
                        item.optString("title", ui("Kampanya", "Campaign")),
                        item.optString("price", ui("Koşulları incele", "Review terms")),
                        description,
                        badge,
                        item.optString("sourceUrl", ""),
                        compact
                ));
                added++;
            }
            return added > 0;
        } catch (Exception ignored) {
            return false;
        }
    }

    private boolean campaignMatchesCountry(JSONObject item) {
        String country = currentCountryCode();
        String countries = campaignCountryText(item);
        if (countries.trim().isEmpty()) {
            return "TR".equals(country);
        }
        String normalized = countries.toUpperCase(Locale.US);
        if (normalized.contains("GLOBAL") || normalized.contains("WORLD") || normalized.contains("ALL")) {
            return true;
        }
        if (normalized.contains(country)) {
            return true;
        }
        return isEuropeanCountry(country) && (normalized.contains("EU") || normalized.contains("EUROPE") || normalized.contains("EUROPA"));
    }

    private String campaignCountryText(JSONObject item) {
        StringBuilder builder = new StringBuilder();
        appendCampaignCountryField(builder, item, "country");
        appendCampaignCountryField(builder, item, "countries");
        appendCampaignCountryField(builder, item, "region");
        appendCampaignCountryField(builder, item, "regions");
        appendCampaignCountryField(builder, item, "market");
        appendCampaignCountryField(builder, item, "markets");
        appendCampaignCountryField(builder, item, "locale");
        appendCampaignCountryField(builder, item, "locales");
        return builder.toString();
    }

    private void appendCampaignCountryField(StringBuilder builder, JSONObject item, String key) {
        JSONArray array = item.optJSONArray(key);
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                builder.append(' ').append(array.optString(i));
            }
            return;
        }
        builder.append(' ').append(item.optString(key, ""));
    }

    private boolean isEuropeanCountry(String country) {
        return "DE".equals(country) || "ES".equals(country) || "FR".equals(country)
                || "PT".equals(country) || "IT".equals(country) || "NL".equals(country)
                || "BE".equals(country) || "AT".equals(country) || "IE".equals(country)
                || "FI".equals(country) || "GR".equals(country);
    }

    private void loadRemoteCampaigns(LinearLayout target, boolean compact) {
        loadRemoteCampaigns(target, compact, null);
    }

    private void loadRemoteCampaigns(LinearLayout target, boolean compact, View container) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(CAMPAIGN_FEED_URL).openConnection();
                connection.setConnectTimeout(12000);
                connection.setReadTimeout(12000);
                connection.setRequestProperty("User-Agent", "AbonelikTakibi-Android/1.0");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                String json = response.toString();
                new JSONObject(json).getJSONArray("campaigns");
                getSharedPreferences(PREFS, MODE_PRIVATE).edit().putString(KEY_CAMPAIGN_CACHE, json).apply();
                runOnUiThread(() -> {
                    if (currentScreen == SCREEN_CAMPAIGNS || currentScreen == SCREEN_HOME) {
                        if (!renderCampaignFeed(target, json, compact)) {
                            renderFallbackCampaignsForCountry(target, compact);
                        }
                        if (container != null) {
                            container.setVisibility(target.getChildCount() == 0 ? View.GONE : View.VISIBLE);
                        }
                    }
                });
            } catch (Exception ignored) {
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    private View campaignCard(String title, String price, String services, String badge, String sourceUrl, boolean compact) {
        if (!badge.startsWith("Sana uygun") && !badge.startsWith("For you") && campaignTextMatchesUser(title + " " + services)) {
            badge = ui("Sana uygun • ", "For you • ") + badge;
        }
        if (!compact) {
            return expandedCampaignCard(title, price, services, badge, sourceUrl);
        }
        boolean expired = badge.startsWith("Süresi doldu") || badge.toLowerCase(Locale.US).startsWith("expired");
        int cardColor = expired ? COLOR_SURFACE_HIGH : Color.rgb(0, 126, 135);
        int primaryText = expired ? COLOR_MUTED : Color.WHITE;

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(12), dp(10), dp(12), dp(10));
        card.setBackground(round(cardColor, dp(8), 0));
        card.setElevation(dp(3));

        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        TextView icon = iconBox("%", expired ? COLOR_SURFACE_LOW : Color.rgb(38, 148, 157), primaryText);
        top.addView(icon, new LinearLayout.LayoutParams(dp(34), dp(34)));
        TextView badgeView = text(expired ? ui("SÜRESİ DOLDU", "EXPIRED") : ui("RESMÎ", "OFFICIAL"), 8, expired ? COLOR_MUTED : Color.rgb(102, 59, 0), Typeface.BOLD);
        badgeView.setGravity(Gravity.CENTER);
        badgeView.setPadding(dp(7), dp(3), dp(7), dp(3));
        badgeView.setBackground(round(expired ? COLOR_SURFACE_LOW : Color.rgb(255, 179, 64), dp(10), 0));
        LinearLayout.LayoutParams badgeParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        badgeParams.setMargins(dp(8), 0, 0, 0);
        top.addView(spacer(dp(1)), new LinearLayout.LayoutParams(0, dp(1), 1));
        top.addView(badgeView, badgeParams);
        card.addView(top);

        TextView titleView = text(title, 13, primaryText, Typeface.BOLD);
        titleView.setMaxLines(2);
        titleView.setPadding(0, dp(9), 0, dp(2));
        card.addView(titleView);
        TextView priceView = text(price, 12, primaryText, Typeface.BOLD);
        priceView.setMaxLines(1);
        card.addView(priceView);
        TextView serviceView = text(services, 10, primaryText, Typeface.NORMAL);
        serviceView.setMaxLines(2);
        serviceView.setPadding(0, dp(5), 0, dp(8));
        card.addView(serviceView);
        TextView action = text(ui("Detaylar", "Details"), 11, Color.WHITE, Typeface.BOLD);
        action.setGravity(Gravity.CENTER);
        action.setBackground(round(expired ? COLOR_MUTED : Color.rgb(35, 145, 154), dp(5), Color.argb(100, 255, 255, 255)));
        card.addView(action, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(30)));

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(dp(180), dp(198));
        cardParams.setMargins(0, 0, dp(12), 0);
        card.setLayoutParams(cardParams);
        card.setAlpha(expired ? 0.72f : 1f);
        card.setOnClickListener(v -> {
            if (!sourceUrl.isEmpty()) {
                openExternalUrl(sourceUrl);
            }
        });
        return card;
    }

    private int campaignMatchScore(JSONObject item) {
        StringBuilder text = new StringBuilder();
        text.append(item.optString("title", "")).append(' ')
                .append(item.optString("description", "")).append(' ')
                .append(item.optString("price", ""));
        JSONArray services = item.optJSONArray("services");
        if (services != null) {
            for (int i = 0; i < services.length(); i++) {
                text.append(' ').append(services.optString(i));
            }
        }
        int score = 0;
        String campaign = normalizeServiceText(text.toString());
        for (ExpenseItem itemRecord : items) {
            String service = normalizeServiceText(itemRecord.name);
            if (service.isEmpty()) continue;
            for (String token : service.split("\\s+")) {
                if (token.length() >= 3 && campaign.contains(token)) {
                    score += 2;
                }
            }
            if (campaign.contains("bein") && service.contains("bein")) score += 4;
            if (campaign.contains("tod") && service.contains("tod")) score += 4;
            if (campaign.contains("prime") && (service.contains("prime") || service.contains("amazon"))) score += 4;
            if (campaign.contains("netflix") && service.contains("netflix")) score += 4;
            if (campaign.contains("youtube") && service.contains("youtube")) score += 4;
            if (campaign.contains("max") && (service.contains("hbo") || service.contains("max"))) score += 4;
        }
        return score;
    }

    private boolean campaignTextMatchesUser(String text) {
        String campaign = normalizeServiceText(text);
        for (ExpenseItem item : items) {
            String service = normalizeServiceText(item.name);
            if (service.length() < 3) continue;
            if ((campaign.contains("bein") && service.contains("bein"))
                    || (campaign.contains("tod") && service.contains("tod"))
                    || (campaign.contains("prime") && (service.contains("prime") || service.contains("amazon")))
                    || (campaign.contains("netflix") && service.contains("netflix"))
                    || (campaign.contains("youtube") && service.contains("youtube"))
                    || (campaign.contains("max") && (service.contains("hbo") || service.contains("max")))) {
                return true;
            }
        }
        return false;
    }

    private View expandedCampaignCard(String title, String price, String services, String badge, String sourceUrl) {
        boolean expired = badge.startsWith("Süresi doldu") || badge.toLowerCase(Locale.US).startsWith("expired");
        LinearLayout card = formCard();
        card.setBackground(round(expired ? COLOR_SURFACE_HIGH : Color.rgb(224, 233, 255), dp(8), COLOR_LINE));
        TextView badgeView = text(expired ? ui("SÜRESİ DOLDU", "EXPIRED") : badge, 11, COLOR_PRIMARY, Typeface.BOLD);
        badgeView.setPadding(dp(9), dp(5), dp(9), dp(5));
        badgeView.setBackground(round(COLOR_SURFACE, dp(12), 0));
        card.addView(badgeView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView titleView = text(title, 20, COLOR_PRIMARY, Typeface.BOLD);
        titleView.setPadding(0, dp(12), 0, dp(4));
        card.addView(titleView);
        card.addView(text(price, 26, COLOR_TEXT, Typeface.BOLD));
        TextView description = text(services, 14, COLOR_MUTED, Typeface.NORMAL);
        description.setPadding(0, dp(9), 0, dp(14));
        card.addView(description);
        TextView details = text(ui("Resmî kampanya sayfasını aç  ›", "Open official campaign page  ›"), 14, COLOR_PRIMARY, Typeface.BOLD);
        card.addView(details);
        card.setAlpha(expired ? 0.72f : 1f);
        card.setOnClickListener(v -> {
            if (!sourceUrl.isEmpty()) {
                openExternalUrl(sourceUrl);
            }
        });
        return card;
    }

    private void openExternalUrl(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception exception) {
            Toast.makeText(this, ui("Sayfa açılamadı.", "Page could not be opened."), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveRecordFromForm(EditText name, EditText amount, Spinner category, CheckBox monthly, Calendar selected,
                                    boolean reminderEnabled, String reminderFrequency, boolean hasEndDate, Calendar endDate) {
        String itemName = name.getText().toString().trim();
        String amountText = amount.getText().toString().trim().replace(',', '.');
        if (itemName.isEmpty() || amountText.isEmpty()) {
            Toast.makeText(this, ui("Ad ve tutar gerekli.", "Name and amount are required."), Toast.LENGTH_SHORT).show();
            return;
        }
        double parsedAmount;
        try {
            parsedAmount = Double.parseDouble(amountText);
        } catch (NumberFormatException ex) {
            Toast.makeText(this, ui("Tutarı kontrol et.", "Check the amount."), Toast.LENGTH_SHORT).show();
            return;
        }
        ExpenseItem target = editingItem == null ? new ExpenseItem() : editingItem;
        if (editingItem == null) {
            target.id = System.currentTimeMillis();
            items.add(target);
        }
        target.name = itemName;
        target.amount = parsedAmount;
        target.category = (String) category.getSelectedItem();
        target.monthly = monthly.isChecked();
        target.day = selected.get(Calendar.DAY_OF_MONTH);
        target.month = selected.get(Calendar.MONTH);
        target.year = selected.get(Calendar.YEAR);
        target.reminderEnabled = reminderEnabled;
        target.reminderFrequency = reminderFrequencyStorageValue(reminderFrequency);
        target.hasEndDate = hasEndDate;
        target.endDay = endDate.get(Calendar.DAY_OF_MONTH);
        target.endMonth = endDate.get(Calendar.MONTH);
        target.endYear = endDate.get(Calendar.YEAR);
        target.paid = false;
        target.paidMonth = -1;
        target.paidYear = -1;
        saveItems();
        scheduleReminders();
        editingItem = null;
        showScreen(returnScreen);
    }

    private void showPersonalInfoDialog() {
        LinearLayout form = new LinearLayout(this);
        form.setOrientation(LinearLayout.VERTICAL);
        form.setPadding(dp(18), dp(8), dp(18), 0);
        form.setBackgroundColor(COLOR_SURFACE);

        TextView dialogTitle = text(ui("Kişisel Bilgiler", "Personal Info"), 22, COLOR_TEXT, Typeface.BOLD);
        dialogTitle.setPadding(0, dp(8), 0, dp(16));
        form.addView(dialogTitle);

        View avatar = profileAvatar(dp(82));
        LinearLayout avatarWrap = new LinearLayout(this);
        avatarWrap.setGravity(Gravity.CENTER);
        avatarWrap.addView(avatar, new LinearLayout.LayoutParams(dp(82), dp(82)));
        form.addView(avatarWrap);

        Button changePhoto = secondaryButton(ui("Resim Değiştir", "Change Photo"));
        changePhoto.setOnClickListener(v -> openProfilePicker());
        LinearLayout.LayoutParams photoParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(48)
        );
        photoParams.setMargins(0, dp(12), 0, dp(16));
        form.addView(changePhoto, photoParams);

        EditText name = field(ui("Ad Soyad", "Full Name"));
        name.setText(getUserName());
        EditText email = field("E-posta");
        email.setText(getUserEmail());
        email.setEnabled(false);
        form.addView(formLabel(ui("Ad Soyad", "Full Name")));
        form.addView(name, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(54)));
        form.addView(spacer(dp(12)));
        form.addView(formLabel(ui("E-posta", "Email")));
        form.addView(email, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(54)));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(form)
                .setNegativeButton(ui("Vazgeç", "Cancel"), null)
                .setPositiveButton(ui("Kaydet", "Save"), (savedDialog, which) -> {
                    getSharedPreferences(PREFS, MODE_PRIVATE)
                            .edit()
                            .putString(KEY_USER_NAME, name.getText().toString().trim().isEmpty() ? ui("Kullanıcı Adı", "User Name") : name.getText().toString().trim())
                            .putString(authNameKey(getUserEmail()), name.getText().toString().trim().isEmpty() ? ui("Kullanıcı Adı", "User Name") : name.getText().toString().trim())
                            .apply();
                    syncToCloud();
                    rebuildCurrentScreen();
                })
                .create();
        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(COLOR_PRIMARY);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(COLOR_MUTED);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(round(COLOR_SURFACE, dp(16), COLOR_LINE));
            }
        });
        dialog.show();
    }

    private void showCurrencyDialog() {
        String[] values = CURRENCY_OPTIONS;
        int checked = Math.max(0, java.util.Arrays.asList(values).indexOf(getCurrency()));
        new AlertDialog.Builder(this)
                .setTitle(ui("Para Birimi", "Currency"))
                .setSingleChoiceItems(values, checked, (dialog, which) -> {
                    getSharedPreferences(PREFS, MODE_PRIVATE).edit().putString(KEY_CURRENCY, values[which]).apply();
                    syncToCloud();
                    dialog.dismiss();
                    rebuildCurrentScreen();
                })
                .show();
    }

    private void showThemeDialog() {
        String[] labels = {ui("Açık", "Light"), ui("Koyu", "Dark"), "Premium"};
        String[] values = {"Açık", "Koyu", "Premium"};
        int checked = Math.max(0, java.util.Arrays.asList(values).indexOf(getThemeName()));
        new AlertDialog.Builder(this)
                .setTitle(ui("Tema", "Theme"))
                .setSingleChoiceItems(labels, checked, (dialog, which) -> {
                    getSharedPreferences(PREFS, MODE_PRIVATE).edit().putString(KEY_THEME, values[which]).apply();
                    syncToCloud();
                    dialog.dismiss();
                    rebuildCurrentScreen();
                })
                .show();
    }

    private void showLanguageDialog() {
        String[] labels = {"English", "Türkçe", "Deutsch", "Español", "Français", "Português"};
        String[] values = {"en", "tr", "de", "es", "fr", "pt"};
        int checked = Math.max(0, java.util.Arrays.asList(values).indexOf(getLanguageCode()));
        new AlertDialog.Builder(this)
                .setTitle(ui("Dil", "Language"))
                .setSingleChoiceItems(labels, checked, (dialog, which) -> {
                    getSharedPreferences(PREFS, MODE_PRIVATE).edit().putString(KEY_LANGUAGE, values[which]).apply();
                    syncToCloud();
                    dialog.dismiss();
                    rebuildCurrentScreen();
                })
                .show();
    }

    private void showNotificationDialog() {
        String[] values = {ui("Aktif", "On"), ui("Kapalı", "Off")};
        int checked = notificationsEnabled() ? 0 : 1;
        new AlertDialog.Builder(this)
                .setTitle(ui("Bildirim Ayarları", "Notification Settings"))
                .setSingleChoiceItems(values, checked, (dialog, which) -> {
                    getSharedPreferences(PREFS, MODE_PRIVATE).edit().putBoolean(KEY_NOTIFICATIONS, which == 0).apply();
                    syncToCloud();
                    dialog.dismiss();
                    rebuildCurrentScreen();
                    if (which == 0) {
                        scheduleReminders();
                    } else {
                        cancelScheduledReminders();
                    }
                })
                .show();
    }

    private void showSecurityDialog() {
        new AlertDialog.Builder(this)
                .setTitle(ui("Güvenlik", "Security"))
                .setMessage(ui("Kayıtların bu cihazda yerel olarak saklanıyor. Profil fotoğrafı için seçilen görsele kalıcı okuma izni alınır.",
                        "Your records are stored locally on this device. Persistent read permission is requested for the selected profile photo."))
                .setPositiveButton(ui("Tamam", "OK"), null)
                .show();
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Abonelik Takibi")
                .setMessage(ui("Sürüm ", "Version ") + "1.0.17\n"
                        + ui("Abonelik, fatura ve gider takibi için kişisel finans yardımcın.",
                        "Your personal finance assistant for tracking subscriptions, bills and expenses."))
                .setPositiveButton(ui("Tamam", "OK"), null)
                .show();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(ui("Çıkış Yap", "Sign Out"))
                .setMessage(ui("Oturum kapatılsın mı? Kayıtların cihazda kalmaya devam eder.",
                        "Do you want to sign out? Your records will remain on this device."))
                .setNegativeButton(ui("Vazgeç", "Cancel"), null)
                .setPositiveButton(ui("Çıkış Yap", "Sign Out"), (dialog, which) -> signOutCompletely())
                .show();
    }

    private void signOutCompletely() {
        cancelScheduledReminders();
        if (firebaseAuth != null) {
            firebaseAuth.signOut();
        }
        if (googleClient != null) {
            googleClient.signOut();
        }
        getSharedPreferences(PREFS, MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_LOGGED_IN, false)
                .apply();
        items.clear();
        screenHistory.clear();
        content.setPadding(dp(24), dp(18), dp(24), dp(20));
        showScreenFromBack(SCREEN_LOGIN);
    }

    private void cancelScheduledReminders() {
        for (ExpenseItem item : items) {
            cancelScheduledReminder(item);
        }
    }

    private void cancelScheduledReminder(ExpenseItem item) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, PaymentReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) item.id, intent,
                PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE);
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    private void render() {
        Calendar now = Calendar.getInstance();
        int currentMonth = now.get(Calendar.MONTH);
        int currentYear = now.get(Calendar.YEAR);
        double total = 0;
        double paid = 0;
        double upcomingAmount = 0;
        int upcomingCount = 0;
        List<ExpenseItem> monthItems = new ArrayList<>();
        List<ExpenseItem> upcoming = new ArrayList<>();
        Map<Long, Boolean> upcomingIds = new HashMap<>();

        for (ExpenseItem item : items) {
            Calendar due = item.dueDateForMonth(now);
            boolean inMonth = itemOccursInMonth(item, now)
                    && due.get(Calendar.MONTH) == currentMonth && due.get(Calendar.YEAR) == currentYear;
            if (inMonth) {
                double monthAmount = item.amountForMonth(currentYear, currentMonth);
                total += monthAmount;
                if (item.isPaidFor(currentYear, currentMonth)) {
                    paid += monthAmount;
                }
                monthItems.add(item);
            }

            due = item.dueDateForMonth(now);
            long days = daysBetween(now, due);
            boolean unpaidCurrentDue = !item.isPaidFor(due.get(Calendar.YEAR), due.get(Calendar.MONTH));
            boolean overdueOrSoon = unpaidCurrentDue && days <= 7;
            if (overdueOrSoon) {
                upcomingCount++;
                upcomingAmount += item.amountForMonth(due.get(Calendar.YEAR), due.get(Calendar.MONTH));
                upcoming.add(item);
                upcomingIds.put(item.id, true);
            }
        }

        totalText.setText(money(total));
        paidText.setText(money(paid));
        double remaining = Math.max(0, total - paid);
        upcomingText.setText(money(remaining));

        renderUpcoming(upcoming, now);
        renderAll(monthItems, now, upcomingIds);
    }

    private void renderUpcoming(List<ExpenseItem> upcoming, Calendar now) {
        upcomingList.removeAllViews();
        Collections.sort(upcoming, Comparator.comparingLong(item -> item.dueDateForMonth(now).getTimeInMillis()));
        if (upcoming.isEmpty()) {
            upcomingList.addView(emptyText(ui("Önümüzdeki 7 gün için bekleyen ödeme yok.", "No pending payment for the next 7 days.")));
            return;
        }
        for (ExpenseItem item : upcoming) {
            Calendar due = item.dueDateForMonth(now);
            long days = daysBetween(now, due);
            String trailing;
            if (days < 0) {
                trailing = formatDate(due);
            } else if (days == 0) {
                trailing = ui("Bugün", "Today");
            } else {
                trailing = days + ui(" gün kaldı", " days left");
            }
            upcomingList.addView(itemRow(item, due, trailing));
        }
    }

    private void renderCategories(Map<String, Double> categoryTotals) {
        categoryList.removeAllViews();
        if (categoryTotals.isEmpty()) {
            categoryList.addView(emptyText(ui("Bu ay için kategori toplamı oluşmadı.", "No category total for this month yet.")));
            return;
        }
        HorizontalScrollView scroll = new HorizontalScrollView(this);
        scroll.setHorizontalScrollBarEnabled(false);
        LinearLayout chips = new LinearLayout(this);
        chips.setOrientation(LinearLayout.HORIZONTAL);
        List<String> categories = new ArrayList<>(categoryTotals.keySet());
        Collections.sort(categories);
        for (String category : categories) {
            chips.addView(categoryChip(category, money(categoryTotals.get(category))));
        }
        scroll.addView(chips);
        categoryList.addView(scroll);
    }

    private void renderAll(List<ExpenseItem> monthItems, Calendar now, Map<Long, Boolean> excludedIds) {
        allItemsList.removeAllViews();
        List<ExpenseItem> visibleItems = new ArrayList<>();
        for (ExpenseItem item : monthItems) {
            if (!excludedIds.containsKey(item.id)) {
                visibleItems.add(item);
            }
        }
        Collections.sort(visibleItems, Comparator.comparingLong(item -> item.dueDateForMonth(now).getTimeInMillis()));
        if (visibleItems.isEmpty()) {
            allItemsList.addView(emptyText(ui("Bu ay için diğer kayıt yok.", "No other records this month.")));
            return;
        }
        for (ExpenseItem item : visibleItems) {
            Calendar due = item.dueDateForMonth(now);
            String trailing = item.isPaidFor(due.get(Calendar.YEAR), due.get(Calendar.MONTH)) ? ui("Ödendi", "Paid") : formatDate(due);
            allItemsList.addView(itemRow(item, due, trailing));
        }
    }

    private View itemRow(ExpenseItem item, Calendar due, String trailing) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        row.setPadding(dp(16), dp(14), dp(16), dp(14));
        row.setBackground(cardBg());
        row.setElevation(dp(2));

        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.HORIZONTAL);
        top.setGravity(Gravity.CENTER_VERTICAL);

        View icon = recordIcon(item);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(dp(44), dp(44));
        iconParams.setMargins(0, 0, dp(14), 0);
        top.addView(icon, iconParams);

        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        TextView name = text(item.name, 16, COLOR_TEXT, Typeface.BOLD);
        String amountHint = item.isEstimatedFor(due.get(Calendar.YEAR), due.get(Calendar.MONTH))
                ? ui("  •  Geçmiş ortalaması", "  •  Past average") : "";
        TextView meta = text(localizedCategory(item.category) + "  •  " + (item.monthly ? ui("Aylık", "Monthly") : shortDate(due)) + amountHint,
                12, COLOR_MUTED, Typeface.NORMAL);
        copy.addView(name);
        copy.addView(meta);
        top.addView(copy, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        LinearLayout price = new LinearLayout(this);
        price.setOrientation(LinearLayout.VERTICAL);
        price.setGravity(Gravity.RIGHT);
        double displayedAmount = item.amountForMonth(due.get(Calendar.YEAR), due.get(Calendar.MONTH));
        TextView amount = text(money(displayedAmount), 16, COLOR_TEXT, Typeface.BOLD);
        boolean paidForThisDate = item.isPaidFor(due.get(Calendar.YEAR), due.get(Calendar.MONTH));
        TextView state = badge(trailing, paidForThisDate);
        price.addView(amount);
        price.addView(state);
        top.addView(price);
        row.addView(top);

        LinearLayout buttons = new LinearLayout(this);
        buttons.setOrientation(LinearLayout.HORIZONTAL);
        buttons.setPadding(0, dp(10), 0, 0);
        View paidButton = smallActionButton(
                paidForThisDate ? ui("Ödenmedi Yap", "Mark Unpaid") : ui("Ödendi Yap", "Mark Paid"),
                R.drawable.ic_action_card,
                paidForThisDate ? COLOR_SURFACE_LOW : Color.rgb(28, 142, 79),
                paidForThisDate ? COLOR_PRIMARY : Color.WHITE
        );
        View editButton = smallActionButton("", R.drawable.ic_action_edit);
        editButton.setContentDescription(ui("Düzenle", "Edit"));
        View deleteButton = smallActionButton("", R.drawable.ic_action_delete);
        deleteButton.setContentDescription(ui("Sil", "Delete"));
        paidButton.setOnClickListener(v -> {
            if (item.isPaidFor(due.get(Calendar.YEAR), due.get(Calendar.MONTH))) {
                item.removeActualPayment(due.get(Calendar.YEAR), due.get(Calendar.MONTH));
                item.paid = false;
                item.paidMonth = -1;
                item.paidYear = -1;
                saveAndRefresh();
            } else if (item.hasVariableMonthlyAmount()) {
                showActualAmountDialog(item, due);
            } else {
                markPaidWithEndWarning(item, due);
            }
        });
        editButton.setOnClickListener(v -> openRecordScreen(item));
        deleteButton.setOnClickListener(v -> confirmDelete(item));
        buttons.addView(paidButton, new LinearLayout.LayoutParams(0, dp(40), 2.0f));
        LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(0, dp(40), 0.45f);
        editParams.setMargins(dp(8), 0, 0, 0);
        buttons.addView(editButton, editParams);
        LinearLayout.LayoutParams deleteParams = new LinearLayout.LayoutParams(0, dp(40), 0.45f);
        deleteParams.setMargins(dp(8), 0, 0, 0);
        buttons.addView(deleteButton, deleteParams);
        row.addView(buttons);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, dp(10));
        row.setLayoutParams(params);
        return row;
    }

    private View smallActionButton(String label, int iconRes) {
        return smallActionButton(label, iconRes, COLOR_SURFACE_LOW, COLOR_PRIMARY);
    }

    private View smallActionButton(String label, int iconRes, int backgroundColor, int foregroundColor) {
        LinearLayout button = new LinearLayout(this);
        button.setOrientation(LinearLayout.HORIZONTAL);
        button.setGravity(Gravity.CENTER);
        button.setPadding(dp(8), 0, dp(8), 0);
        button.setBackground(round(backgroundColor, dp(8), 0));
        button.setClickable(true);
        button.setFocusable(true);

        ImageView icon = new ImageView(this);
        icon.setImageResource(iconRes);
        Drawable drawable = icon.getDrawable();
        if (drawable != null) {
            drawable.setTint(foregroundColor);
        }
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(dp(16), dp(16));
        iconParams.setMargins(0, 0, label == null || label.isEmpty() ? 0 : dp(6), 0);
        button.addView(icon, iconParams);

        if (label != null && !label.isEmpty()) {
            TextView text = text(label, 11, foregroundColor, Typeface.BOLD);
            text.setSingleLine(true);
            button.addView(text, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return button;
    }

    private void showActualAmountDialog(ExpenseItem item, Calendar due) {
        EditText input = field(ui("Gerçekleşen tutar", "Actual amount"));
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setText(decimalFormat().format(
                item.amountForMonth(due.get(Calendar.YEAR), due.get(Calendar.MONTH))));
        input.setSelectAllOnFocus(true);

        LinearLayout body = new LinearLayout(this);
        body.setOrientation(LinearLayout.VERTICAL);
        body.setPadding(dp(20), dp(4), dp(20), 0);
        body.setBackgroundColor(COLOR_SURFACE);
        body.addView(text(localizedCategory(item.category) + ui(" faturası • ", " bill • ") + formatDate(due), 14, COLOR_MUTED, Typeface.BOLD));
        body.addView(text(ui("Faturada yazan gerçekleşen tutarı para birimi formatına uygun girin (ör. 1.250,50).",
                        "Enter the actual amount shown on the bill in the proper currency format (e.g. 1,250.50)."),
                13, COLOR_MUTED, Typeface.NORMAL));
        if (item.isFinalMonth(due)) {
            TextView warning = text(ui("Bu son ay, tarifeniz bitiyor.", "This is the final month; your plan is ending."), 14, Color.rgb(147, 45, 35), Typeface.BOLD);
            warning.setPadding(0, dp(10), 0, 0);
            body.addView(warning);
        }
        LinearLayout.LayoutParams inputParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(56));
        inputParams.setMargins(0, dp(12), 0, 0);
        body.addView(input, inputParams);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(item.name + ui(" ödendi", " paid"))
                .setView(body)
                .setNegativeButton(ui("Vazgeç", "Cancel"), null)
                .setPositiveButton(ui("Kaydet", "Save"), null)
                .create();
        dialog.setOnShowListener(ignored -> {
            styleAppDialog(dialog);
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                try {
                    double actual = parseLocalizedAmount(input.getText().toString());
                    if (actual <= 0) throw new NumberFormatException();
                    item.setActualPayment(due.get(Calendar.YEAR), due.get(Calendar.MONTH), actual);
                    item.paid = true;
                    item.paidMonth = due.get(Calendar.MONTH);
                    item.paidYear = due.get(Calendar.YEAR);
                    dialog.dismiss();
                    saveAndRefresh();
                } catch (NumberFormatException exception) {
                    input.setError(ui("Geçerli bir tutar girin", "Enter a valid amount"));
                }
            });
        });
        dialog.show();
    }

    private void markPaidWithEndWarning(ExpenseItem item, Calendar due) {
        Runnable markPaid = () -> {
            item.paid = true;
            item.paidMonth = due.get(Calendar.MONTH);
            item.paidYear = due.get(Calendar.YEAR);
            saveAndRefresh();
        };
        if (!item.isFinalMonth(due)) {
            markPaid.run();
            return;
        }
        LinearLayout body = new LinearLayout(this);
        body.setOrientation(LinearLayout.VERTICAL);
        body.setPadding(dp(20), dp(18), dp(20), dp(4));
        body.setBackgroundColor(COLOR_SURFACE);
        body.addView(text(ui("Son tarife ödemesi", "Final plan payment"), 21, COLOR_TEXT, Typeface.BOLD));
        TextView message = text(ui("Bu son ay, tarifeniz bitiyor.", "This is the final month; your plan is ending."),
                15, COLOR_MUTED, Typeface.NORMAL);
        message.setPadding(0, dp(10), 0, 0);
        body.addView(message);
        body.addView(text(item.name + " • " + formatDate(due), 13, COLOR_PRIMARY, Typeface.BOLD));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(body)
                .setNegativeButton(ui("Vazgeç", "Cancel"), null)
                .setPositiveButton(ui("Ödendi yap", "Mark paid"), (ignored, which) -> markPaid.run())
                .create();
        dialog.setOnShowListener(ignored -> styleAppDialog(dialog));
        dialog.show();
    }

    private void showItemDetailDialog(ExpenseItem item, Calendar due) {
        LinearLayout body = new LinearLayout(this);
        body.setOrientation(LinearLayout.VERTICAL);
        body.setPadding(dp(20), dp(8), dp(20), 0);
        body.addView(text(localizedCategory(item.category), 13, categoryTextColor(item.category), Typeface.BOLD));
        body.addView(text(money(item.amountForMonth(due.get(Calendar.YEAR), due.get(Calendar.MONTH))),
                26, COLOR_PRIMARY, Typeface.BOLD));
        body.addView(text(ui("Ödeme tarihi: ", "Payment date: ") + formatDate(due), 14, COLOR_MUTED, Typeface.NORMAL));
        body.addView(text(ui("Durum: ", "Status: ") + (item.isPaidFor(due.get(Calendar.YEAR), due.get(Calendar.MONTH)) ? ui("Ödendi", "Paid") : ui("Bekliyor", "Pending")),
                14, COLOR_MUTED, Typeface.NORMAL));
        body.addView(text(ui("Bildirim: ", "Reminder: ") + (item.reminderEnabled ? localizedReminderFrequency(item.reminderFrequency) : ui("Kapalı", "Off")),
                14, COLOR_MUTED, Typeface.NORMAL));
        body.addView(text(ui("Bitiş: ", "End: ") + (item.hasEndDate ? formatDate(item.endDate()) : ui("Bitiş tarihi yok", "No end date")),
                14, COLOR_MUTED, Typeface.NORMAL));
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(item.name)
                .setView(body)
                .setNegativeButton(ui("Kapat", "Close"), null)
                .setPositiveButton(ui("Düzenle", "Edit"), (ignored, which) -> openRecordScreen(item))
                .create();
        dialog.setOnShowListener(ignored -> styleAppDialog(dialog));
        dialog.show();
    }

    private void showReminderFrequencyDialog(String[] options, String[] selected, TextView label) {
        LinearLayout body = new LinearLayout(this);
        body.setOrientation(LinearLayout.VERTICAL);
        body.setPadding(dp(18), dp(18), dp(18), dp(10));
        body.setBackgroundColor(COLOR_SURFACE);
        TextView title = text(ui("Bildirim sıklığı", "Reminder frequency"), 21, COLOR_TEXT, Typeface.BOLD);
        title.setPadding(dp(4), 0, dp(4), dp(12));
        body.addView(title);

        AlertDialog[] holder = new AlertDialog[1];
        for (String option : options) {
            boolean active = option.equals(selected[0]);
            TextView choice = text((active ? "✓  " : "   ") + option, 16,
                    active ? COLOR_PRIMARY : COLOR_TEXT, active ? Typeface.BOLD : Typeface.NORMAL);
            choice.setGravity(Gravity.CENTER_VERTICAL);
            choice.setPadding(dp(16), dp(12), dp(16), dp(12));
            choice.setBackground(round(active ? Color.rgb(226, 244, 246) : COLOR_SURFACE_LOW, dp(12),
                    active ? COLOR_PRIMARY_CONTAINER : 0));
            choice.setOnClickListener(v -> {
                selected[0] = option;
                label.setText(ui("Bildirim sıklığı: ", "Reminder frequency: ") + option + "  ▾");
                holder[0].dismiss();
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, dp(52));
            params.setMargins(0, 0, 0, dp(8));
            body.addView(choice, params);
        }

        holder[0] = new AlertDialog.Builder(this)
                .setView(body)
                .setNegativeButton(ui("Vazgeç", "Cancel"), null)
                .create();
        holder[0].setOnShowListener(ignored -> styleAppDialog(holder[0]));
        holder[0].show();
    }

    private void styleAppDialog(AlertDialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(round(COLOR_SURFACE, dp(22), COLOR_LINE));
        }
        int titleId = getResources().getIdentifier("alertTitle", "id", "android");
        TextView title = dialog.findViewById(titleId);
        if (title != null) {
            title.setTextColor(COLOR_TEXT);
        }
        Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        Button neutral = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        if (positive != null) positive.setTextColor(COLOR_PRIMARY);
        if (negative != null) negative.setTextColor(COLOR_MUTED);
        if (neutral != null) neutral.setTextColor(COLOR_MUTED);
    }

    private int reminderFrequencyIndex(String frequency) {
        String storageValue = reminderFrequencyStorageValue(frequency);
        if ("Haftalık".equals(storageValue)) return 1;
        if ("Aylık".equals(storageValue)) return 2;
        if ("Yıllık".equals(storageValue)) return 3;
        return 0;
    }

    private String localizedReminderFrequency(String frequency) {
        String storageValue = reminderFrequencyStorageValue(frequency);
        if ("Haftalık".equals(storageValue)) return ui("Haftalık", "Weekly");
        if ("Aylık".equals(storageValue)) return ui("Aylık", "Monthly");
        if ("Yıllık".equals(storageValue)) return ui("Yıllık", "Yearly");
        return ui("Günlük", "Daily");
    }

    private String reminderFrequencyStorageValue(String frequency) {
        if (frequency == null) return "Günlük";
        String normalized = frequency.trim().toLowerCase(Locale.US);
        if ("weekly".equals(normalized) || "haftalık".equalsIgnoreCase(frequency)) return "Haftalık";
        if ("monthly".equals(normalized) || "aylık".equalsIgnoreCase(frequency)) return "Aylık";
        if ("yearly".equals(normalized) || "annual".equals(normalized) || "yıllık".equalsIgnoreCase(frequency)) return "Yıllık";
        return "Günlük";
    }

    private String endDateLabel(boolean hasEndDate, Calendar date) {
        return hasEndDate
                ? ui("Bitiş Tarihi: ", "End Date: ") + formatDate(date)
                : ui("Bitiş tarihi yok (seçmek için dokun)", "No end date (tap to select)");
    }

    private void showEndDateDialog(TextView label, Calendar selected, boolean[] hasEndDate) {
        new AlertDialog.Builder(this)
                .setTitle(ui("Abonelik bitiş tarihi", "Subscription end date"))
                .setItems(new String[]{ui("Tarih seç", "Select date"), ui("Bitiş tarihini kaldır", "Remove end date")}, (dialog, which) -> {
                    if (which == 1) {
                        hasEndDate[0] = false;
                        label.setText(endDateLabel(false, selected));
                        return;
                    }
                    new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                        selected.set(year, month, dayOfMonth);
                        hasEndDate[0] = true;
                        label.setText(endDateLabel(true, selected));
                    }, selected.get(Calendar.YEAR), selected.get(Calendar.MONTH),
                            selected.get(Calendar.DAY_OF_MONTH)).show();
                }).show();
    }

    private double parseLocalizedAmount(String value) throws NumberFormatException {
        String clean = value.trim()
                .replace("₺", "")
                .replace("€", "")
                .replace("$", "")
                .replace("£", "")
                .replace("₹", "")
                .replace("¥", "")
                .replace(" ", "");
        if (clean.contains(",")) {
            clean = clean.replace(".", "").replace(',', '.');
        }
        return Double.parseDouble(clean);
    }

    private void moveToReminderDate(Calendar reminder, String frequency) {
        String storageValue = reminderFrequencyStorageValue(frequency);
        if ("Yıllık".equals(storageValue)) reminder.add(Calendar.YEAR, -1);
        else if ("Aylık".equals(storageValue)) reminder.add(Calendar.MONTH, -1);
        else if ("Haftalık".equals(storageValue)) reminder.add(Calendar.DAY_OF_MONTH, -7);
        else reminder.add(Calendar.DAY_OF_MONTH, -1);
    }

    private void showEditDialog(ExpenseItem existing, boolean defaultMonthly) {
        LinearLayout form = new LinearLayout(this);
        form.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(18);
        form.setPadding(pad, dp(8), pad, 0);

        EditText name = field("Ad");
        name.setText(existing == null ? "" : existing.name);
        EditText amount = field("Tutar");
        amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        amount.setText(existing == null ? "" : String.valueOf(existing.amount));

        Spinner category = new Spinner(this);
        category.setAdapter(categoryAdapter());
        category.setSelection(categoryIndex(existing == null ? "Abonelik" : existing.category));

        CheckBox monthly = new CheckBox(this);
        monthly.setText("Her ay tekrar eder");
        monthly.setTextColor(COLOR_TEXT);
        monthly.setChecked(existing == null ? defaultMonthly : existing.monthly);

        TextView date = text("", 15, COLOR_PRIMARY, Typeface.BOLD);
        Calendar selected = Calendar.getInstance();
        if (existing != null) {
            selected.set(existing.year, existing.month, existing.day);
        }
        updateDateLabel(date, selected);
        date.setPadding(0, dp(12), 0, dp(12));
        date.setOnClickListener(v -> new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selected.set(year, month, dayOfMonth);
                    updateDateLabel(date, selected);
                },
                selected.get(Calendar.YEAR),
                selected.get(Calendar.MONTH),
                selected.get(Calendar.DAY_OF_MONTH)
        ).show());

        form.addView(name);
        form.addView(amount);
        form.addView(category, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(52)
        ));
        form.addView(monthly);
        form.addView(date);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(existing == null ? ui("Yeni kayıt", "New record") : ui("Kaydı düzenle", "Edit record"))
                .setView(form)
                .setNegativeButton(ui("Vazgeç", "Cancel"), null)
                .setPositiveButton(ui("Kaydet", "Save"), null)
                .create();
        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String itemName = name.getText().toString().trim();
            String amountText = amount.getText().toString().trim().replace(',', '.');
            if (itemName.isEmpty() || amountText.isEmpty()) {
                Toast.makeText(this, ui("Ad ve tutar gerekli.", "Name and amount are required."), Toast.LENGTH_SHORT).show();
                return;
            }
            double parsedAmount;
            try {
                parsedAmount = Double.parseDouble(amountText);
            } catch (NumberFormatException ex) {
                Toast.makeText(this, ui("Tutarı kontrol et.", "Check the amount."), Toast.LENGTH_SHORT).show();
                return;
            }
            ExpenseItem target = existing == null ? new ExpenseItem() : existing;
            if (existing == null) {
                target.id = System.currentTimeMillis();
                items.add(target);
            }
            target.name = itemName;
            target.amount = parsedAmount;
            target.category = (String) category.getSelectedItem();
            target.monthly = monthly.isChecked();
            target.day = selected.get(Calendar.DAY_OF_MONTH);
            target.month = selected.get(Calendar.MONTH);
            target.year = selected.get(Calendar.YEAR);
            target.paid = false;
            target.paidMonth = -1;
            target.paidYear = -1;
            saveAndRefresh();
            dialog.dismiss();
        }));
        dialog.show();
    }

    private void confirmDelete(ExpenseItem item) {
        new AlertDialog.Builder(this)
                .setTitle(ui("Kaydı sil", "Delete record"))
                .setMessage(item.name + ui(" silinsin mi?", " will be deleted. Continue?"))
                .setNegativeButton(ui("Vazgeç", "Cancel"), null)
                .setPositiveButton(ui("Sil", "Delete"), (dialog, which) -> {
                    cancelScheduledReminder(item);
                    items.remove(item);
                    saveAndRefresh();
                })
                .show();
    }

    private void confirmDeleteFromEdit(ExpenseItem item) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(ui("Kaydı sil", "Delete record"))
                .setMessage(item.name + ui(" kalıcı olarak silinsin mi?", " will be permanently deleted. Continue?"))
                .setNegativeButton(ui("Vazgeç", "Cancel"), null)
                .setPositiveButton(ui("Sil", "Delete"), (ignored, which) -> {
                    cancelScheduledReminder(item);
                    items.remove(item);
                    saveItems();
                    editingItem = null;
                    showScreen(returnScreen);
                    scheduleReminders();
                })
                .create();
        dialog.setOnShowListener(ignored -> {
            styleAppDialog(dialog);
            Button delete = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            if (delete != null) delete.setTextColor(Color.rgb(147, 45, 35));
        });
        dialog.show();
    }

    private void openCancellationPage(ExpenseItem item) {
        String service = item.name.toLowerCase(new Locale("tr", "TR"));
        String url;
        if (service.contains("netflix")) {
            url = "https://www.netflix.com/cancelplan";
        } else if (service.contains("spotify")) {
            url = "https://www.spotify.com/account/subscription/cancel/";
        } else if (service.contains("youtube")) {
            url = "https://www.youtube.com/paid_memberships";
        } else if (service.contains("disney")) {
            url = "https://www.disneyplus.com/account";
        } else if (service.contains("amazon") || service.contains("prime")) {
            url = "https://www.amazon.com/hz5/yourmembershipsandsubscriptions";
        } else if (service.contains("max") || service.contains("blutv")) {
            url = "https://www.max.com/account";
        } else if (service.contains("tabii") || service.contains("tabi")) {
            url = "https://www.tabii.com/profile";
        } else if (service.contains("exxen")) {
            url = "https://www.exxen.com/tr/account";
        } else if (service.contains("gain") || service.contains("gaın")) {
            url = "https://www.gain.tv/profile";
        } else if (service.contains("tod") || service.contains("bein connect")) {
            url = "https://www.todtv.com.tr/account";
        } else {
            return;
        }
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception exception) {
            Toast.makeText(this, ui("İptal sayfası açılamadı.", "Cancellation page could not be opened."), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isCancellableService(ExpenseItem item) {
        String service = item.name.toLowerCase(new Locale("tr", "TR"));
        return service.contains("netflix")
                || service.contains("spotify")
                || service.contains("youtube")
                || service.contains("disney")
                || service.contains("amazon")
                || service.contains("prime")
                || service.contains("tabii")
                || service.contains("tabi")
                || service.contains("exxen")
                || service.contains("gain")
                || service.contains("gaın")
                || service.contains("tod")
                || service.contains("bein connect")
                || service.contains("max")
                || service.contains("blutv");
    }

    private void saveAndRefresh() {
        saveItems();
        showScreen(currentScreen);
        scheduleReminders();
    }

    private String reminderTitle(ExpenseItem item, Calendar due, double amount) {
        long days = daysBetween(Calendar.getInstance(), due);
        if (days == 1) {
            return item.name + reminderRenewsTomorrowText();
        }
        if (days == 0) {
            return item.name + reminderRenewsTodayText();
        }
        return item.name + ui(" ödeme zamanı", " payment reminder");
    }

    private String reminderMessage(ExpenseItem item, Calendar due, double amount) {
        long days = daysBetween(Calendar.getInstance(), due);
        String price = money(amount);
        String language = getLanguageCode();
        if (days == 1) {
            if ("tr".equals(language)) return "Yarın " + price + " ödeyeceksin.";
            if ("de".equals(language)) return "Du zahlst morgen " + price + ".";
            if ("es".equals(language)) return "Pagarás " + price + " mañana.";
            if ("fr".equals(language)) return "Vous paierez " + price + " demain.";
            if ("pt".equals(language)) return "Você pagará " + price + " amanhã.";
            return "You'll pay " + price + " tomorrow.";
        }
        if (days == 0) {
            if ("tr".equals(language)) return "Bugün " + price + " ödeme var.";
            if ("de".equals(language)) return "Du zahlst heute " + price + ".";
            if ("es".equals(language)) return "Pagarás " + price + " hoy.";
            if ("fr".equals(language)) return "Vous paierez " + price + " aujourd'hui.";
            if ("pt".equals(language)) return "Você pagará " + price + " hoje.";
            return "You'll pay " + price + " today.";
        }
        return formatDate(due) + ui(" tarihinde ", " has ")
                + price + ui(" ödeme var.", " payment due.");
    }

    private String reminderRenewsTomorrowText() {
        String language = getLanguageCode();
        if ("tr".equals(language)) return " yarın yenileniyor.";
        if ("de".equals(language)) return " verlängert sich morgen.";
        if ("es".equals(language)) return " se renueva mañana.";
        if ("fr".equals(language)) return " se renouvelle demain.";
        if ("pt".equals(language)) return " renova amanhã.";
        return " renews tomorrow.";
    }

    private String reminderRenewsTodayText() {
        String language = getLanguageCode();
        if ("tr".equals(language)) return " bugün yenileniyor.";
        if ("de".equals(language)) return " verlängert sich heute.";
        if ("es".equals(language)) return " se renueva hoy.";
        if ("fr".equals(language)) return " se renouvelle aujourd'hui.";
        if ("pt".equals(language)) return " renova hoje.";
        return " renews today.";
    }

    private void scheduleReminders() {
        cancelScheduledReminders();
        if (!notificationsEnabled()) {
            return;
        }
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar now = Calendar.getInstance();
        for (ExpenseItem item : items) {
            if (!item.reminderEnabled) {
                continue;
            }
            Calendar due = item.nextReminderDate(now);
            if (!itemOccursInMonth(item, due)) {
                continue;
            }
            if (item.isPaidFor(due.get(Calendar.YEAR), due.get(Calendar.MONTH))) {
                continue;
            }
            Calendar remindAt = (Calendar) due.clone();
            moveToReminderDate(remindAt, item.reminderFrequency);
            remindAt.set(Calendar.HOUR_OF_DAY, 9);
            remindAt.set(Calendar.MINUTE, 0);
            remindAt.set(Calendar.SECOND, 0);
            if (remindAt.before(now)) {
                remindAt = (Calendar) due.clone();
                remindAt.set(Calendar.HOUR_OF_DAY, 9);
                remindAt.set(Calendar.MINUTE, 0);
            }
            if (remindAt.before(now)) {
                continue;
            }
            double amount = item.amountForMonth(due.get(Calendar.YEAR), due.get(Calendar.MONTH));
            Intent intent = new Intent(this, PaymentReminderReceiver.class);
            intent.putExtra(PaymentReminderReceiver.EXTRA_TITLE, reminderTitle(item, due, amount));
            intent.putExtra(PaymentReminderReceiver.EXTRA_MESSAGE, reminderMessage(item, due, amount));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    (int) item.id,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            alarmManager.set(AlarmManager.RTC_WAKEUP, remindAt.getTimeInMillis(), pendingIntent);
        }
    }

    private void requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= 33
                && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 45);
        }
    }

    private void loadItems() {
        items.clear();
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        migrateLegacyItemsIfNeeded(prefs);
        String json = prefs.getString(currentItemsKey(), "[]");
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                items.add(ExpenseItem.fromJson(array.getJSONObject(i)));
            }
        } catch (Exception ignored) {
            items.clear();
        }
    }

    private void saveItems() {
        saveItemsLocally();
        syncToCloud();
    }

    private void saveItemsLocally() {
        JSONArray array = new JSONArray();
        for (ExpenseItem item : items) {
            array.put(item.toJson());
        }
        getSharedPreferences(PREFS, MODE_PRIVATE)
                .edit()
                .putString(currentItemsKey(), array.toString())
                .apply();
    }

    private String itemsJson() {
        JSONArray array = new JSONArray();
        for (ExpenseItem item : items) {
            array.put(item.toJson());
        }
        return array.toString();
    }

    private JSONObject backupJson() {
        JSONObject backup = new JSONObject();
        try {
            backup.put("schema", "subscription_tracker_storage_backup_v1");
            backup.put("items_json", itemsJson());
            backup.put("user_name", getUserName());
            backup.put("user_email", getUserEmail());
            backup.put("currency", getCurrency());
            backup.put("country", currentCountryCode());
            backup.put("theme", getThemeName());
            backup.put("language", getLanguageCode());
            backup.put("notifications", notificationsEnabled());
            backup.put("updated_at", System.currentTimeMillis());
        } catch (Exception ignored) {
        }
        return backup;
    }

    private StorageReference backupStorageRef() {
        if (storage == null || firebaseAuth == null || firebaseAuth.getCurrentUser() == null) {
            return null;
        }
        return storage.getReference()
                .child("backups")
                .child(firebaseAuth.getCurrentUser().getUid())
                .child("subscription_tracker_backup.json");
    }

    private void uploadStorageBackup() {
        uploadStorageBackup(false);
    }

    private void uploadStorageBackup(boolean showFailure) {
        StorageReference ref = backupStorageRef();
        if (ref == null) {
            return;
        }
        byte[] payload = backupJson().toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
        ref.putBytes(payload)
                .addOnSuccessListener(task -> getSharedPreferences(PREFS, MODE_PRIVATE)
                        .edit()
                        .putLong(KEY_LAST_CLOUD_SYNC, System.currentTimeMillis())
                        .apply())
                .addOnFailureListener(e -> {
                    if (showFailure && !isStorageObjectMissing(e)) {
                        Toast.makeText(this, ui("Storage yedeği alınamadı: ", "Storage backup failed: ") + safeMessage(e), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void backupBeforeAppLeaves() {
        if (applyingCloudData || cloudLoadInProgress || !isLoggedIn()) {
            return;
        }
        syncToCloud();
        uploadStorageBackup(false);
    }

    private boolean isStorageObjectMissing(Exception e) {
        return e instanceof StorageException
                && ((StorageException) e).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND;
    }

    private void restoreFromStorageBackupOrCreate() {
        cloudLoadFinished();
        if (!items.isEmpty()) {
            syncToCloud();
        }
    }

    private void applyCloudBackup(JSONObject backup) {
        String cloudItemsJson = backup.optString("items_json", itemsJson());
        if (isEmptyItemsJson(cloudItemsJson) && !items.isEmpty()) {
            cloudItemsJson = itemsJson();
        }
        String signedInEmail = firebaseAuth != null
                && firebaseAuth.getCurrentUser() != null
                && firebaseAuth.getCurrentUser().getEmail() != null
                ? firebaseAuth.getCurrentUser().getEmail()
                : getUserEmail();
        applyingCloudData = true;
        SharedPreferences.Editor editor = getSharedPreferences(PREFS, MODE_PRIVATE).edit()
                .putString(currentItemsKey(), cloudItemsJson)
                .putString(KEY_USER_NAME, backup.optString("user_name", getUserName()))
                .putString(KEY_USER_EMAIL, signedInEmail)
                .putString(KEY_CURRENCY, backup.optString("currency", getCurrency()))
                .putString(KEY_COUNTRY, backup.optString("country", currentCountryCode()))
                .putString(KEY_THEME, backup.optString("theme", getThemeName()))
                .putString(KEY_LANGUAGE, backup.optString("language", getLanguageCode()))
                .putBoolean(KEY_NOTIFICATIONS, backup.optBoolean("notifications", notificationsEnabled()))
                .putBoolean(KEY_COUNTRY_PROMPT_COMPLETED, backup.has("country") && !backup.optString("country", "").trim().isEmpty())
                .putBoolean(countryPromptCompletedKey(signedInEmail), backup.has("country") && !backup.optString("country", "").trim().isEmpty())
                .putLong(KEY_LAST_CLOUD_SYNC, System.currentTimeMillis());
        editor.apply();
        applyThemeColors();
        loadItems();
        applyingCloudData = false;
        scheduleReminders();
        if (content != null && currentScreen == SCREEN_HOME) {
            showScreen(SCREEN_HOME);
        }
    }

    private void syncToCloud() {
        if (applyingCloudData) {
            return;
        }
        if (firestore == null || firebaseAuth == null || firebaseAuth.getCurrentUser() == null) {
            notifyCloudSyncUnavailable();
            return;
        }
        long updatedAt = System.currentTimeMillis();
        Map<String, Object> data = cloudData(updatedAt);
        DocumentReference primaryDoc = primaryUserDocument();
        if (primaryDoc == null) {
            notifyCloudSyncUnavailable();
            return;
        }
        primaryDoc.set(data)
                .addOnSuccessListener(unused -> {
                    markCloudSyncComplete();
                    uploadStorageBackup();
                })
                .addOnFailureListener(e -> Toast.makeText(this, ui("Bulut senkron başarısız: ", "Cloud sync failed: ") + safeMessage(e), Toast.LENGTH_LONG).show());
    }

    private void notifyCloudSyncUnavailable() {
        if (cloudSyncWarningShown) {
            return;
        }
        cloudSyncWarningShown = true;
        Toast.makeText(
                this,
                ui("Bulut yedekleme kapalı. Lütfen hesabına yeniden giriş yap.", "Cloud backup is unavailable. Please sign in again."),
                Toast.LENGTH_LONG
        ).show();
    }

    private void syncSubscriptionDocuments(Map<String, Object> data, long updatedAt) {
        DocumentReference userDoc = currentUserDocument();
        if (userDoc == null) {
            return;
        }
        userDoc.collection(CLOUD_SUBSCRIPTIONS)
                .get()
                .addOnSuccessListener(snapshot -> writeCloudBatch(userDoc, snapshot.getDocuments(), data, updatedAt))
                .addOnFailureListener(e -> writeCloudBatch(userDoc, new ArrayList<>(), data, updatedAt));
    }

    private void writeCloudBatch(DocumentReference userDoc, List<DocumentSnapshot> existingDocs,
                                 Map<String, Object> data, long updatedAt) {
        String backupId = backupDocumentId(updatedAt);
        WriteBatch batch = firestore.batch();
        batch.set(userDoc, data);
        batch.set(userDoc.collection(CLOUD_BACKUPS).document(backupId), data);
        Map<String, Boolean> activeIds = new HashMap<>();
        for (ExpenseItem item : items) {
            String id = subscriptionDocumentId(item);
            activeIds.put(id, true);
            batch.set(userDoc.collection(CLOUD_SUBSCRIPTIONS).document(id), subscriptionData(item, updatedAt, false));
        }
        for (DocumentSnapshot doc : existingDocs) {
            if (!activeIds.containsKey(doc.getId())) {
                Map<String, Object> deleted = new HashMap<>();
                deleted.put("deleted", true);
                deleted.put("updated_at", updatedAt);
                batch.set(doc.getReference(), deleted, com.google.firebase.firestore.SetOptions.merge());
            }
        }
        String emailDoc = cloudEmailDocumentId();
        if (!emailDoc.isEmpty()) {
            DocumentReference emailRef = firestore.collection(CLOUD_EMAIL_BACKUPS).document(emailDoc);
            batch.set(emailRef, data);
            batch.set(emailRef.collection(CLOUD_BACKUPS).document(backupId), data);
        }
        batch.commit()
                .addOnSuccessListener(unused -> {
                    markCloudSyncComplete();
                    uploadStorageBackup();
                })
                .addOnFailureListener(e -> Toast.makeText(this, ui("Bulut senkron başarısız: ", "Cloud sync failed: ") + safeMessage(e), Toast.LENGTH_LONG).show());
    }

    private Map<String, Object> subscriptionData(ExpenseItem item, long updatedAt, boolean deleted) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", item.id);
        data.put("item_json", item.toJson().toString());
        data.put("deleted", deleted);
        data.put("updated_at", updatedAt);
        return data;
    }

    private String subscriptionDocumentId(ExpenseItem item) {
        return String.valueOf(item.id);
    }

    private DocumentReference currentUserDocument() {
        if (firestore == null || firebaseAuth == null || firebaseAuth.getCurrentUser() == null) {
            return null;
        }
        return firestore.collection(CLOUD_USERS).document(firebaseAuth.getCurrentUser().getUid());
    }

    private DocumentReference primaryUserDocument() {
        if (firestore == null || firebaseAuth == null || firebaseAuth.getCurrentUser() == null) {
            return null;
        }
        return firestore.collection(CLOUD_PRIMARY_USERS).document(firebaseAuth.getCurrentUser().getUid());
    }

    private Map<String, Object> cloudData(long updatedAt) {
        Map<String, Object> data = new HashMap<>();
        data.put("items_json", itemsJson());
        data.put("user_name", getUserName());
        data.put("user_email", getUserEmail());
        data.put("currency", getCurrency());
        data.put("country", currentCountryCode());
        data.put("theme", getThemeName());
        data.put("language", getLanguageCode());
        data.put("notifications", notificationsEnabled());
        data.put("updated_at", updatedAt);
        return data;
    }

    private String backupDocumentId(long updatedAt) {
        return "backup_" + updatedAt;
    }

    private void markCloudSyncComplete() {
        getSharedPreferences(PREFS, MODE_PRIVATE)
                .edit()
                .putLong(KEY_LAST_CLOUD_SYNC, System.currentTimeMillis())
                .apply();
    }

    private void loadCloudData() {
        cloudLoadInProgress = true;
        loadFirestoreDataOrCreate();
    }

    private void cloudLoadFinished() {
        cloudLoadInProgress = false;
        if (pendingCountryPromptAfterCloud) {
            pendingCountryPromptAfterCloud = false;
            ensureCountrySelectionPrompt();
        }
    }

    private void loadFirestoreDataOrCreate() {
        if (firestore == null || firebaseAuth == null || firebaseAuth.getCurrentUser() == null) {
            cloudLoadFinished();
            return;
        }
        DocumentReference primaryDoc = primaryUserDocument();
        if (primaryDoc == null) {
            cloudLoadFinished();
            return;
        }
        DocumentReference userDoc = currentUserDocument();
        if (userDoc == null) {
            cloudLoadFinished();
            return;
        }
        if (!items.isEmpty()) {
            cloudLoadFinished();
            syncToCloud();
            return;
        }
        primaryDoc
                .get()
                .addOnSuccessListener(primarySnapshot -> {
                    JSONObject primaryBackup = backupFromFirestoreDoc(primarySnapshot);
                    if (primaryBackup != null) {
                        if (isEmptyItemsJson(primaryBackup.optString("items_json", "[]")) && !items.isEmpty()) {
                            cloudLoadFinished();
                            syncToCloud();
                            return;
                        }
                        applyCloudBackup(primaryBackup);
                        cloudLoadFinished();
                        uploadStorageBackup();
                        return;
                    }
                    loadLegacyCloudData(userDoc);
                })
                .addOnFailureListener(e -> loadLegacyCloudData(userDoc));
    }

    private void loadLegacyCloudData(DocumentReference userDoc) {
        userDoc.collection(CLOUD_SUBSCRIPTIONS)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (applySubscriptionDocuments(snapshot.getDocuments())) {
                        cloudLoadFinished();
                        uploadStorageBackup();
                        syncToCloud();
                    } else {
                        loadLegacyCloudBackup(userDoc);
                    }
                })
                .addOnFailureListener(e -> loadLegacyCloudBackup(userDoc));
    }

    private void loadLegacyCloudBackup(DocumentReference userDoc) {
        userDoc
                .get()
                .addOnSuccessListener(uidDoc -> loadLatestUserBackupThenEmail(userDoc, uidDoc))
                .addOnFailureListener(e -> {
                    if (items.isEmpty()) {
                        restoreFromStorageBackupOrCreate();
                    } else {
                        cloudLoadFinished();
                        syncToCloud();
                    }
                });
    }

    private boolean applySubscriptionDocuments(List<DocumentSnapshot> docs) {
        if (docs.isEmpty()) {
            return false;
        }
        List<ExpenseItem> cloudItems = new ArrayList<>();
        for (DocumentSnapshot doc : docs) {
            Boolean deleted = doc.getBoolean("deleted");
            if (deleted != null && deleted) {
                continue;
            }
            String itemJson = doc.getString("item_json");
            if (itemJson == null || itemJson.trim().isEmpty()) {
                continue;
            }
            try {
                cloudItems.add(ExpenseItem.fromJson(new JSONObject(itemJson)));
            } catch (Exception ignored) {
            }
        }
        items.clear();
        items.addAll(cloudItems);
        saveItemsLocally();
        scheduleReminders();
        if (content != null && currentScreen == SCREEN_HOME) {
            showScreen(SCREEN_HOME);
        }
        return true;
    }

    private void loadLatestUserBackupThenEmail(DocumentReference userDoc, DocumentSnapshot uidDoc) {
        userDoc.collection(CLOUD_BACKUPS)
                .orderBy("updated_at", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(snapshot -> {
                    DocumentSnapshot uidBackupDoc = snapshot.isEmpty() ? null : snapshot.getDocuments().get(0);
                    loadEmailBackupsThenApply(uidDoc, uidBackupDoc);
                })
                .addOnFailureListener(e -> loadEmailBackupsThenApply(uidDoc, null));
    }

    private void loadEmailBackupsThenApply(DocumentSnapshot uidDoc, DocumentSnapshot uidBackupDoc) {
        String emailDoc = cloudEmailDocumentId();
        if (emailDoc.isEmpty()) {
            loadUserByEmailThenApply(uidDoc, uidBackupDoc, null, null);
            return;
        }
        DocumentReference emailRef = firestore.collection(CLOUD_EMAIL_BACKUPS).document(emailDoc);
        emailRef
                .get()
                .addOnSuccessListener(emailLatestDoc -> emailRef.collection(CLOUD_BACKUPS)
                        .orderBy("updated_at", Query.Direction.DESCENDING)
                        .limit(1)
                        .get()
                        .addOnSuccessListener(snapshot -> {
                            DocumentSnapshot emailBackupDoc = snapshot.isEmpty() ? null : snapshot.getDocuments().get(0);
                            loadUserByEmailThenApply(uidDoc, uidBackupDoc, emailLatestDoc, emailBackupDoc);
                        })
                        .addOnFailureListener(e -> loadUserByEmailThenApply(uidDoc, uidBackupDoc, emailLatestDoc, null)))
                .addOnFailureListener(e -> loadUserByEmailThenApply(uidDoc, uidBackupDoc, null, null));
    }

    private void loadUserByEmailThenApply(DocumentSnapshot uidDoc, DocumentSnapshot uidBackupDoc,
                                          DocumentSnapshot emailDoc, DocumentSnapshot emailBackupDoc) {
        String email = getUserEmail();
        if (firestore == null || email == null || email.trim().isEmpty()) {
            applyBestFirestoreBackup(uidDoc, uidBackupDoc, emailDoc, emailBackupDoc, null);
            return;
        }
        firestore.collection(CLOUD_PRIMARY_USERS)
                .whereEqualTo("user_email", email.trim())
                .limit(1)
                .get()
                .addOnSuccessListener(snapshot -> {
                    DocumentSnapshot userByEmail = snapshot.isEmpty() ? null : snapshot.getDocuments().get(0);
                    applyBestFirestoreBackup(uidDoc, uidBackupDoc, emailDoc, emailBackupDoc, userByEmail);
                })
                .addOnFailureListener(e -> applyBestFirestoreBackup(uidDoc, uidBackupDoc, emailDoc, emailBackupDoc, null));
    }

    private void applyBestFirestoreBackup(DocumentSnapshot uidDoc, DocumentSnapshot uidBackupDoc,
                                          DocumentSnapshot emailDoc, DocumentSnapshot emailBackupDoc,
                                          DocumentSnapshot userByEmailDoc) {
        List<JSONObject> backups = new ArrayList<>();
        addBackupIfPresent(backups, backupFromFirestoreDoc(uidDoc));
        addBackupIfPresent(backups, backupFromFirestoreDoc(uidBackupDoc));
        addBackupIfPresent(backups, backupFromFirestoreDoc(emailDoc));
        addBackupIfPresent(backups, backupFromFirestoreDoc(emailBackupDoc));
        addBackupIfPresent(backups, backupFromFirestoreDoc(userByEmailDoc));
        JSONObject selected = newestNonEmptyBackup(backups);
        if (selected == null && !backups.isEmpty()) {
            selected = backups.get(0);
        }
        if (selected == null) {
            restoreFromStorageBackupOrCreate();
            return;
        }
        if (isEmptyItemsJson(selected.optString("items_json", "[]")) && !items.isEmpty()) {
            cloudLoadFinished();
            syncToCloud();
            return;
        }
        applyCloudBackup(selected);
        cloudLoadFinished();
        uploadStorageBackup();
        syncToCloud();
    }

    private void addBackupIfPresent(List<JSONObject> backups, JSONObject backup) {
        if (backup != null) {
            backups.add(backup);
        }
    }

    private JSONObject newestNonEmptyBackup(List<JSONObject> backups) {
        JSONObject selected = null;
        for (JSONObject backup : backups) {
            if (isEmptyItemsJson(backup.optString("items_json", "[]"))) {
                continue;
            }
            if (selected == null || backup.optLong("updated_at", 0) > selected.optLong("updated_at", 0)) {
                selected = backup;
            }
        }
        return selected;
    }

    private JSONObject backupFromFirestoreDoc(DocumentSnapshot doc) {
        if (doc == null || !doc.exists()) {
            return null;
        }
        String itemsJson = doc.getString("items_json");
        String country = doc.getString("country");
        if (itemsJson == null && (country == null || country.trim().isEmpty())) {
            return null;
        }
        JSONObject backup = new JSONObject();
        try {
            backup.put("items_json", itemsJson == null ? "[]" : itemsJson);
            backup.put("user_name", doc.getString("user_name") == null ? getUserName() : doc.getString("user_name"));
            backup.put("user_email", doc.getString("user_email") == null ? getUserEmail() : doc.getString("user_email"));
            backup.put("currency", doc.getString("currency") == null ? getCurrency() : doc.getString("currency"));
            backup.put("country", country == null ? "" : country);
            backup.put("theme", doc.getString("theme") == null ? getThemeName() : doc.getString("theme"));
            backup.put("language", doc.getString("language") == null ? getLanguageCode() : doc.getString("language"));
            Boolean notifications = doc.getBoolean("notifications");
            backup.put("notifications", notifications == null ? notificationsEnabled() : notifications);
            Long updatedAt = doc.getLong("updated_at");
            backup.put("updated_at", updatedAt == null ? 0 : updatedAt);
        } catch (Exception ignored) {
        }
        return backup;
    }

    private String cloudEmailDocumentId() {
        String email = getUserEmail();
        if (email == null || email.trim().isEmpty() || "kullanici@email.com".equalsIgnoreCase(email.trim())) {
            return "";
        }
        return normalizeEmail(email);
    }

    private boolean isEmptyItemsJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return true;
        }
        try {
            return new JSONArray(json).length() == 0;
        } catch (Exception ignored) {
            return false;
        }
    }

    private void migrateLegacyItemsIfNeeded(SharedPreferences prefs) {
        String key = currentItemsKey();
        if (prefs.contains(key) || !prefs.contains(KEY_ITEMS) || "[]".equals(prefs.getString(KEY_ITEMS, "[]"))) {
            return;
        }
        prefs.edit()
                .putString(key, prefs.getString(KEY_ITEMS, "[]"))
                .remove(KEY_ITEMS)
                .apply();
    }

    private String currentItemsKey() {
        return itemsKeyForEmail(getUserEmail());
    }

    private String itemsKeyForEmail(String email) {
        return "items_" + normalizeEmail(email);
    }

    private String authPasswordKey(String email) {
        return "auth_password_" + normalizeEmail(email);
    }

    private String authNameKey(String email) {
        return "auth_name_" + normalizeEmail(email);
    }

    private String authProviderKey(String email) {
        return "auth_provider_" + normalizeEmail(email);
    }

    private String profileUriKey(String email) {
        return "profile_uri_" + normalizeEmail(email);
    }

    private void migrateLegacyProfileIfNeeded() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String accountKey = profileUriKey(getUserEmail());
        if (!prefs.contains(accountKey) && prefs.contains(KEY_PROFILE_URI)) {
            prefs.edit()
                    .putString(accountKey, prefs.getString(KEY_PROFILE_URI, null))
                    .remove(KEY_PROFILE_URI)
                    .apply();
        }
    }

    private String normalizeEmail(String email) {
        if (email == null) {
            return "guest";
        }
        String normalized = email.trim().toLowerCase(Locale.US);
        if (normalized.isEmpty()) {
            return "guest";
        }
        return normalized.replaceAll("[^a-z0-9._-]", "_");
    }

    private void seedDemoItemsIfEmpty() {
        if (!items.isEmpty()) {
            return;
        }
        Calendar now = Calendar.getInstance();
        items.add(ExpenseItem.create("Netflix", 229.99, "Abonelik", true, now, false));
        Calendar market = Calendar.getInstance();
        market.add(Calendar.DAY_OF_MONTH, -2);
        items.add(ExpenseItem.create("Haftalık market", 1420, "Market", false, market, true));
        Calendar electric = Calendar.getInstance();
        electric.add(Calendar.DAY_OF_MONTH, 4);
        items.add(ExpenseItem.create("Elektrik faturası", 680, "Elektrik", false, electric, false));
        saveItems();
    }

    private String getUserName() {
        String name = getSharedPreferences(PREFS, MODE_PRIVATE).getString(KEY_USER_NAME, "");
        return name == null || name.trim().isEmpty() || "Kullanıcı Adı".equals(name) ? ui("Kullanıcı Adı", "User Name") : name;
    }

    private String getUserEmail() {
        if (firebaseAuth != null && firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().getEmail() != null) {
            return firebaseAuth.getCurrentUser().getEmail();
        }
        return getSharedPreferences(PREFS, MODE_PRIVATE).getString(KEY_USER_EMAIL, "");
    }

    private String getCurrency() {
        String currency = getSharedPreferences(PREFS, MODE_PRIVATE).getString(KEY_CURRENCY, "TRY");
        return "TL".equals(currency) ? "TRY" : currency;
    }

    private String getThemeName() {
        return getSharedPreferences(PREFS, MODE_PRIVATE).getString(KEY_THEME, "Açık");
    }

    private String getThemeDisplayName() {
        String theme = getThemeName();
        if ("Koyu".equals(theme)) return ui("Koyu", "Dark");
        if ("Premium".equals(theme)) return "Premium";
        return ui("Açık", "Light");
    }

    private boolean notificationsEnabled() {
        return getSharedPreferences(PREFS, MODE_PRIVATE).getBoolean(KEY_NOTIFICATIONS, true);
    }

    private boolean isLoggedIn() {
        return firebaseAuth != null && firebaseAuth.getCurrentUser() != null;
    }

    private String safeMessage(Exception e) {
        return e == null
                ? ui("Bilinmeyen hata", "Unknown error")
                : (e.getLocalizedMessage() == null ? e.getClass().getSimpleName() : e.getLocalizedMessage());
    }

    private String googleSignInMessage(Exception e) {
        return e instanceof ApiException
                ? "Kod " + ((ApiException) e).getStatusCode() + ": " + safeMessage(e)
                : safeMessage(e);
    }

    private void applyThemeColors() {
        String theme = getThemeName();
        boolean dark = "Koyu".equals(theme);
        if (dark) {
            COLOR_BG = Color.rgb(18, 24, 25);
            COLOR_SURFACE = Color.rgb(31, 38, 39);
            COLOR_SURFACE_LOW = Color.rgb(40, 48, 49);
            COLOR_SURFACE_HIGH = Color.rgb(54, 63, 64);
            COLOR_TEXT = Color.rgb(240, 244, 244);
            COLOR_MUTED = Color.rgb(190, 202, 204);
            COLOR_PRIMARY = Color.rgb(130, 211, 222);
            COLOR_PRIMARY_CONTAINER = Color.rgb(0, 109, 119);
            COLOR_ACCENT = Color.rgb(255, 184, 107);
            COLOR_ACCENT_CONTAINER = Color.rgb(253, 157, 26);
            COLOR_LINE = Color.rgb(92, 108, 110);
        } else if ("Premium".equals(theme)) {
            COLOR_BG = Color.rgb(250, 246, 238);
            COLOR_SURFACE = Color.rgb(255, 252, 246);
            COLOR_SURFACE_LOW = Color.rgb(244, 235, 218);
            COLOR_SURFACE_HIGH = Color.rgb(232, 218, 192);
            COLOR_TEXT = Color.rgb(32, 36, 34);
            COLOR_MUTED = Color.rgb(101, 93, 80);
            COLOR_PRIMARY = Color.rgb(20, 75, 67);
            COLOR_PRIMARY_CONTAINER = Color.rgb(10, 101, 94);
            COLOR_ACCENT = Color.rgb(141, 91, 18);
            COLOR_ACCENT_CONTAINER = Color.rgb(223, 166, 78);
            COLOR_LINE = Color.rgb(224, 211, 187);
        } else {
            COLOR_BG = Color.rgb(248, 249, 250);
            COLOR_SURFACE = Color.WHITE;
            COLOR_SURFACE_LOW = Color.rgb(243, 244, 245);
            COLOR_SURFACE_HIGH = Color.rgb(231, 232, 233);
            COLOR_TEXT = Color.rgb(25, 28, 29);
            COLOR_MUTED = Color.rgb(62, 73, 74);
            COLOR_PRIMARY = Color.rgb(0, 83, 91);
            COLOR_PRIMARY_CONTAINER = Color.rgb(0, 109, 119);
            COLOR_ACCENT = Color.rgb(137, 81, 0);
            COLOR_ACCENT_CONTAINER = Color.rgb(253, 157, 26);
            COLOR_LINE = Color.rgb(190, 200, 202);
        }
    }

    private String money(double amount) {
        return currencySymbol() + decimalFormat().format(amount);
    }

    private NumberFormat decimalFormat() {
        NumberFormat format = NumberFormat.getNumberInstance(appLocale());
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        return format;
    }

    private String currencySymbol() {
        String currency = getCurrency();
        if ("USD".equals(currency)) return "$";
        if ("EUR".equals(currency)) return "€";
        if ("GBP".equals(currency)) return "£";
        if ("CAD".equals(currency)) return "C$";
        if ("AUD".equals(currency)) return "A$";
        if ("INR".equals(currency)) return "₹";
        if ("JPY".equals(currency)) return "¥";
        return "₺";
    }

    private void rebuildCurrentScreen() {
        applyThemeColors();
        getWindow().setStatusBarColor(COLOR_BG);
        getWindow().setNavigationBarColor(COLOR_SURFACE);
        setContentView(createContentView());
        showScreen(currentScreen == SCREEN_RECORD ? returnScreen : currentScreen);
    }

    private Map<String, Double> categoryTotalsForFilter() {
        Map<String, Double> totals = new HashMap<>();
        for (ExpenseItem item : items) {
            if (itemOccursInMonth(item, selectedReportMonth)) {
                double monthAmount = item.amountForMonth(
                        selectedReportMonth.get(Calendar.YEAR), selectedReportMonth.get(Calendar.MONTH));
                totals.put(item.category, totals.getOrDefault(item.category, 0.0) + monthAmount);
            }
        }
        return totals;
    }

    private int countItemsForCategory(String category) {
        int count = 0;
        for (ExpenseItem item : items) {
            if (category.equals(item.category)) {
                count++;
            }
        }
        return count;
    }

    private int countItemsForCategoryFilter(String category) {
        int count = 0;
        for (ExpenseItem item : items) {
            if (category.equals(item.category) && itemOccursInMonth(item, selectedReportMonth)) {
                count++;
            }
        }
        return count;
    }

    private List<ExpenseItem> itemsForCurrentMonth() {
        return itemsForMonth(Calendar.getInstance());
    }

    private List<ExpenseItem> itemsForMonth(Calendar now) {
        List<ExpenseItem> monthItems = new ArrayList<>();
        for (ExpenseItem item : items) {
            if (!itemOccursInMonth(item, now)) {
                continue;
            }
            Calendar due = item.dueDateForMonth(now);
            if (due.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                    && due.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
                monthItems.add(item);
            }
        }
        return monthItems;
    }

    private View summaryChips() {
        HorizontalScrollView scroll = new HorizontalScrollView(this);
        scroll.setHorizontalScrollBarEnabled(false);
        LinearLayout chips = new LinearLayout(this);
        chips.setOrientation(LinearLayout.HORIZONTAL);
        chips.setPadding(0, dp(16), 0, dp(22));
        chips.addView(navChip(ui("Tüm Kayıtlar", "All Records") + " (" + items.size() + ")", 0));
        chips.addView(navChip(ui("En Çok Harcanan", "Top Spending"), 1));
        scroll.addView(chips);
        return scroll;
    }

    private TextView navChip(String label, int filter) {
        boolean active = categoryFilter == filter;
        TextView chip = text(label, 12, active ? Color.WHITE : COLOR_MUTED, Typeface.BOLD);
        chip.setGravity(Gravity.CENTER);
        chip.setPadding(dp(14), dp(8), dp(14), dp(8));
        chip.setBackground(round(active ? COLOR_PRIMARY_CONTAINER : COLOR_SURFACE_HIGH, dp(100), active ? 0 : COLOR_LINE));
        chip.setOnClickListener(v -> {
            categoryFilter = filter;
            showScreen(SCREEN_CATEGORIES);
        });
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, dp(8), 0);
        chip.setLayoutParams(params);
        return chip;
    }

    private View categoryCard(String category, String amount, String count) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding(dp(16), dp(14), dp(16), dp(14));
        card.setBackground(round(categoryTint(category), dp(8), categoryTextColor(category)));
        card.setElevation(dp(2));

        TextView icon = iconBox(categoryIcon(category), categoryTint(category), categoryTextColor(category));
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(dp(48), dp(48));
        iconParams.setMargins(0, 0, dp(14), 0);
        card.addView(icon, iconParams);

        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.addView(text(localizedCategory(category), 18, COLOR_TEXT, Typeface.BOLD));
        copy.addView(text(count, 14, COLOR_MUTED, Typeface.NORMAL));
        card.addView(copy, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        TextView price = text(amount, 16, categoryTextColor(category), Typeface.BOLD);
        card.addView(price);
        card.setOnClickListener(v -> {
            selectedCategoryDetail = category.equals(selectedCategoryDetail) ? null : category;
            showScreen(SCREEN_CATEGORIES);
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, dp(12));
        card.setLayoutParams(params);
        return card;
    }

    private View calendarCard(Calendar now) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(16), dp(16), dp(16));
        card.setBackground(cardBg());
        card.setElevation(dp(2));

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        TextView title = text(new SimpleDateFormat("MMMM yyyy", appLocale()).format(now.getTime()) + "  ▾", 18, COLOR_TEXT, Typeface.BOLD);
        title.setOnClickListener(v -> showMonthYearPicker());
        header.addView(title, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        TextView prev = text("‹", 26, COLOR_MUTED, Typeface.BOLD);
        TextView next = text("›", 26, COLOR_MUTED, Typeface.BOLD);
        prev.setGravity(Gravity.CENTER);
        next.setGravity(Gravity.CENTER);
        prev.setOnClickListener(v -> changeCalendarMonth(-1));
        next.setOnClickListener(v -> changeCalendarMonth(1));
        header.addView(prev, new LinearLayout.LayoutParams(dp(34), dp(34)));
        header.addView(next, new LinearLayout.LayoutParams(dp(34), dp(34)));
        card.addView(header);

        LinearLayout days = new LinearLayout(this);
        days.setOrientation(LinearLayout.VERTICAL);
        String[] week = {"Pt", "Sa", "Ça", "Pe", "Cu", "Ct", "Pz"};
        LinearLayout names = new LinearLayout(this);
        for (String day : week) {
            TextView name = text(day, 12, COLOR_MUTED, Typeface.BOLD);
            name.setGravity(Gravity.CENTER);
            names.addView(name, new LinearLayout.LayoutParams(0, dp(28), 1));
        }
        days.addView(names);

        Calendar first = (Calendar) now.clone();
        first.set(Calendar.DAY_OF_MONTH, 1);
        int offset = first.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ? 6 : first.get(Calendar.DAY_OF_WEEK) - 2;
        int max = first.getActualMaximum(Calendar.DAY_OF_MONTH);
        int day = 1 - offset;
        for (int row = 0; row < 6; row++) {
            LinearLayout line = new LinearLayout(this);
            for (int col = 0; col < 7; col++) {
                TextView cell = calendarDay(day, max, now);
                line.addView(cell, new LinearLayout.LayoutParams(0, dp(42), 1));
                day++;
            }
            days.addView(line);
        }
        card.addView(days);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, dp(16), 0, dp(24));
        card.setLayoutParams(params);
        return card;
    }

    private void changeCalendarMonth(int amount) {
        displayedCalendar.add(Calendar.MONTH, amount);
        displayedCalendar.set(Calendar.DAY_OF_MONTH, 1);
        selectedCalendarDay = 1;
        showScreen(SCREEN_CALENDAR);
    }

    private void showMonthYearPicker() {
        LinearLayout picker = new LinearLayout(this);
        picker.setOrientation(LinearLayout.HORIZONTAL);
        picker.setPadding(dp(18), dp(8), dp(18), 0);

        String[] monthNames = new String[12];
        Calendar sample = Calendar.getInstance();
        sample.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", appLocale());
        for (int i = 0; i < monthNames.length; i++) {
            sample.set(Calendar.MONTH, i);
            monthNames[i] = monthFormat.format(sample.getTime());
        }

        int centerYear = displayedCalendar.get(Calendar.YEAR);
        String[] years = new String[41];
        for (int i = 0; i < years.length; i++) {
            years[i] = String.valueOf(centerYear - 20 + i);
        }

        Spinner monthSpinner = new Spinner(this);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, monthNames);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);
        monthSpinner.setSelection(displayedCalendar.get(Calendar.MONTH));

        Spinner yearSpinner = new Spinner(this);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
        yearSpinner.setSelection(20);

        picker.addView(monthSpinner, new LinearLayout.LayoutParams(0, dp(56), 1));
        picker.addView(yearSpinner, new LinearLayout.LayoutParams(0, dp(56), 1));

        new AlertDialog.Builder(this)
                .setTitle(ui("Ay ve Yıl Seç", "Select Month and Year"))
                .setView(picker)
                .setNegativeButton(ui("Vazgeç", "Cancel"), null)
                .setPositiveButton(ui("Göster", "Show"), (dialog, which) -> {
                    displayedCalendar.set(Calendar.YEAR, Integer.parseInt(years[yearSpinner.getSelectedItemPosition()]));
                    displayedCalendar.set(Calendar.MONTH, monthSpinner.getSelectedItemPosition());
                    displayedCalendar.set(Calendar.DAY_OF_MONTH, 1);
                    selectedCalendarDay = 1;
                    showScreen(SCREEN_CALENDAR);
                })
                .show();
    }

    private TextView calendarDay(int day, int max, Calendar now) {
        boolean inMonth = day >= 1 && day <= max;
        String marker = inMonth && hasPaymentOnDay(day, now) ? "\n•" : "";
        TextView cell = text(inMonth ? String.valueOf(day) + marker : "", 14, COLOR_TEXT, Typeface.BOLD);
        cell.setGravity(Gravity.CENTER);
        if (inMonth && day == selectedCalendarDay) {
            cell.setTextColor(Color.WHITE);
            cell.setBackground(round(COLOR_PRIMARY_CONTAINER, dp(12), 0));
        } else if (inMonth && hasPaymentOnDay(day, now)) {
            String category = firstCategoryOnDay(day, now);
            cell.setTextColor(categoryTextColor(category));
            cell.setBackground(round(categoryTint(category), dp(12), 0));
        }
        if (inMonth) {
            cell.setOnClickListener(v -> {
                selectedCalendarDay = day;
                showScreen(SCREEN_CALENDAR);
            });
        }
        return cell;
    }

    private View dayGroupHeader(Calendar date, double total) {
        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM);
        header.setPadding(0, dp(14), 0, dp(10));
        String label = new SimpleDateFormat("d MMMM, EEEE", appLocale()).format(date.getTime());
        header.addView(text(label, 17, COLOR_TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        header.addView(text(money(total), 12, COLOR_PRIMARY, Typeface.BOLD));
        return header;
    }

    private double totalForDay(int day, Calendar now) {
        double total = 0;
        for (ExpenseItem item : itemsForDay(day, now)) {
            total += item.amountForMonth(now.get(Calendar.YEAR), now.get(Calendar.MONTH));
        }
        return total;
    }

    private String firstCategoryOnDay(int day, Calendar now) {
        List<ExpenseItem> dayItems = itemsForDay(day, now);
        if (dayItems.isEmpty()) {
            return "Abonelik";
        }
        return dayItems.get(0).category;
    }

    private boolean hasPaymentOnDay(int day, Calendar now) {
        for (ExpenseItem item : items) {
            if (!itemOccursInMonth(item, now)) {
                continue;
            }
            Calendar due = item.dueDateForMonth(now);
            if (due.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                    && due.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                    && due.get(Calendar.DAY_OF_MONTH) == day) {
                return true;
            }
        }
        return false;
    }

    private LinearLayout calendarTopBar() {
        LinearLayout bar = new LinearLayout(this);
        bar.setGravity(Gravity.CENTER_VERTICAL);
        bar.setPadding(0, dp(8), 0, dp(16));
        TextView title = text(ui("Takvim", "Calendar"), 24, COLOR_PRIMARY, Typeface.BOLD);
        bar.addView(title, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        TextView search = text("⌕", 24, COLOR_MUTED, Typeface.BOLD);
        search.setGravity(Gravity.CENTER);
        bar.addView(search, new LinearLayout.LayoutParams(dp(40), dp(48)));
        int size = dp(TOP_AVATAR_SIZE_DP);
        View profile = profileAvatar(size);
        profile.setOnClickListener(v -> showScreen(SCREEN_SETTINGS));
        bar.addView(profile, new LinearLayout.LayoutParams(size, size));
        return bar;
    }

    private List<ExpenseItem> itemsForDay(int day, Calendar now) {
        List<ExpenseItem> result = new ArrayList<>();
        for (ExpenseItem item : items) {
            if (!itemOccursInMonth(item, now)) {
                continue;
            }
            Calendar due = item.dueDateForMonth(now);
            if (due.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                    && due.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                    && due.get(Calendar.DAY_OF_MONTH) == day) {
                result.add(item);
            }
        }
        return result;
    }

    private View calendarPaymentRow(ExpenseItem item, Calendar due) {
        LinearLayout row = new LinearLayout(this);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(16), dp(14), dp(16), dp(14));
        row.setBackground(cardBg());
        row.setElevation(dp(2));
        View icon = recordIcon(item);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(dp(44), dp(44));
        iconParams.setMargins(0, 0, dp(14), 0);
        row.addView(icon, iconParams);

        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.addView(text(item.name, 16, COLOR_TEXT, Typeface.BOLD));
        String amountHint = item.isEstimatedFor(due.get(Calendar.YEAR), due.get(Calendar.MONTH))
                ? ui(" • Geçmiş ortalaması", " • Past average") : "";
        copy.addView(text(localizedCategory(item.category) + " • " + (item.monthly ? ui("Aylık", "Monthly") : shortDate(due)) + amountHint,
                12, COLOR_MUTED, Typeface.NORMAL));
        row.addView(copy, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        LinearLayout right = new LinearLayout(this);
        right.setOrientation(LinearLayout.VERTICAL);
        right.setGravity(Gravity.RIGHT);
        right.addView(text(money(item.amountForMonth(due.get(Calendar.YEAR), due.get(Calendar.MONTH))),
                16, COLOR_PRIMARY, Typeface.BOLD));
        right.addView(text(item.monthly ? ui("Aylık", "Monthly") : ui("Tek sefer", "One-time"), 11, COLOR_MUTED, Typeface.BOLD));
        row.addView(right);
        row.setOnClickListener(v -> showItemDetailDialog(item, due));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, dp(12));
        row.setLayoutParams(params);
        return row;
    }

    private View profileCard() {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding(dp(16), dp(16), dp(16), dp(16));
        card.setBackground(cardBg());
        card.setElevation(dp(2));
        card.setOnClickListener(v -> showPersonalInfoDialog());
        LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(dp(72), dp(72));
        avatarParams.setMargins(0, 0, dp(16), 0);
        card.addView(profileAvatar(dp(72)), avatarParams);

        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.addView(text(getUserName(), 18, COLOR_TEXT, Typeface.BOLD));
        copy.addView(text(getUserEmail(), 14, COLOR_MUTED, Typeface.NORMAL));
        card.addView(copy, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        card.addView(text("›", 28, COLOR_MUTED, Typeface.BOLD));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, dp(16), 0, dp(24));
        card.setLayoutParams(params);
        return card;
    }

    private LinearLayout settingsGroup(String title, View... rows) {
        LinearLayout group = new LinearLayout(this);
        group.setOrientation(LinearLayout.VERTICAL);
        group.setPadding(0, dp(12), 0, dp(12));
        group.setBackground(cardBg());
        group.setElevation(dp(2));
        TextView header = text(title.toUpperCase(appLocale()), 12, COLOR_MUTED, Typeface.BOLD);
        header.setPadding(dp(16), 0, dp(16), dp(4));
        group.addView(header);
        for (View row : rows) {
            group.addView(row);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, dp(12));
        group.setLayoutParams(params);
        return group;
    }

    private View settingRow(String title, String value, String iconText, View.OnClickListener listener) {
        LinearLayout row = new LinearLayout(this);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(16), dp(12), dp(16), dp(12));
        row.setOnClickListener(listener);
        TextView icon = iconBox(iconText, Color.rgb(226, 244, 246), COLOR_PRIMARY);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(dp(40), dp(40));
        iconParams.setMargins(0, 0, dp(14), 0);
        row.addView(icon, iconParams);
        row.addView(text(title, 16, COLOR_TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        TextView valueText = text(value + "  ›", 14, COLOR_MUTED, Typeface.BOLD);
        row.addView(valueText);
        return row;
    }

    private LinearLayout formCard() {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(18), dp(16), dp(18));
        card.setBackground(cardBg());
        card.setElevation(dp(2));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, dp(24));
        card.setLayoutParams(params);
        return card;
    }

    private TextView formLabel(String value) {
        TextView label = text(value, 14, COLOR_TEXT, Typeface.NORMAL);
        label.setPadding(0, 0, 0, dp(8));
        return label;
    }

    private TextView fieldLikeText(String value) {
        TextView view = text(value, 16, COLOR_TEXT, Typeface.NORMAL);
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setPadding(dp(14), 0, dp(14), 0);
        view.setBackground(round(COLOR_SURFACE, dp(8), COLOR_LINE));
        return view;
    }

    private CheckBox styledCheck(String title, String subtitle) {
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(title + "\n" + subtitle);
        checkBox.setTextSize(16);
        checkBox.setTextColor(COLOR_TEXT);
        checkBox.setPadding(0, dp(12), 0, dp(12));
        return checkBox;
    }

    private ArrayAdapter<String> categoryAdapter() {
        return new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CATEGORIES) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setText(localizedCategory(CATEGORIES[position]));
                view.setTextColor(COLOR_TEXT);
                view.setTextSize(16);
                view.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                view.setPadding(dp(14), 0, dp(14), 0);
                view.setBackgroundColor(Color.TRANSPARENT);
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                view.setText(localizedCategory(CATEGORIES[position]));
                view.setTextColor(COLOR_TEXT);
                view.setTextSize(16);
                view.setPadding(dp(14), dp(12), dp(14), dp(12));
                view.setBackgroundColor(COLOR_SURFACE);
                return view;
            }
        };
    }

    private View divider() {
        View view = new View(this);
        view.setBackgroundColor(Color.rgb(231, 232, 233));
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(1)));
        return view;
    }

    private View spacer(int height) {
        View view = new View(this);
        view.setLayoutParams(new LinearLayout.LayoutParams(1, height));
        return view;
    }

    private LinearLayout topAppBar(String titleText, boolean showLogo) {
        LinearLayout bar = new LinearLayout(this);
        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setGravity(Gravity.CENTER_VERTICAL);
        bar.setPadding(0, dp(8), 0, dp(8));

        if (showLogo) {
            LinearLayout brand = new LinearLayout(this);
            brand.setGravity(Gravity.CENTER_VERTICAL);
            brand.setOrientation(LinearLayout.HORIZONTAL);
            brand.addView(appMarkIcon(), new LinearLayout.LayoutParams(dp(46), dp(46)));
            TextView brandName = text(ui("Abonelik Takibi", "Subscription Tracker"), 22, COLOR_PRIMARY, Typeface.BOLD);
            LinearLayout.LayoutParams brandNameParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            brandNameParams.setMargins(dp(12), 0, 0, 0);
            brand.addView(brandName, brandNameParams);
            bar.addView(brand, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        } else {
            TextView title = text(titleText, 24, COLOR_PRIMARY, Typeface.BOLD);
            bar.addView(title, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        }

        View profile = profileAvatar(dp(TOP_AVATAR_SIZE_DP));
        profile.setOnClickListener(v -> showScreen(SCREEN_SETTINGS));
        bar.addView(profile, new LinearLayout.LayoutParams(dp(TOP_AVATAR_SIZE_DP), dp(TOP_AVATAR_SIZE_DP)));
        return bar;
    }

    private View profileAvatar(int size) {
        FrameLayout frame = new FrameLayout(this);
        frame.setBackground(round(COLOR_SURFACE_HIGH, size / 2, COLOR_LINE));
        frame.setClipToOutline(true);
        frame.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, size, size);
            }
        });
        SharedPreferences profilePrefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String uri = profilePrefs.getString(profileUriKey(getUserEmail()), null);
        if (uri == null && PROVIDER_GOOGLE.equals(profilePrefs.getString(authProviderKey(getUserEmail()), ""))) {
            uri = findGoogleProfileUri();
        }
        TextView initials = text(userInitials(), size >= dp(70) ? 20 : 14, COLOR_MUTED, Typeface.BOLD);
        initials.setGravity(Gravity.CENTER);
        frame.addView(initials, new FrameLayout.LayoutParams(size, size));
        if (PROVIDER_GOOGLE.equals(profilePrefs.getString(authProviderKey(getUserEmail()), ""))) {
            TextView googleBadge = text("G", size >= dp(70) ? 12 : 9, Color.rgb(66, 133, 244), Typeface.BOLD);
            googleBadge.setGravity(Gravity.CENTER);
            googleBadge.setBackground(round(Color.WHITE, dp(10), COLOR_LINE));
            FrameLayout.LayoutParams badgeParams = new FrameLayout.LayoutParams(size / 3, size / 3, Gravity.END | Gravity.BOTTOM);
            frame.addView(googleBadge, badgeParams);
        }
        if (uri != null) {
            ImageView image = new ImageView(this);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setClipToOutline(true);
            frame.addView(image, new FrameLayout.LayoutParams(size, size));
            if (uri.startsWith("http://") || uri.startsWith("https://")) {
                String remoteUri = uri;
                new Thread(() -> {
                    try {
                        android.graphics.Bitmap bitmap = BitmapFactory.decodeStream(new URL(remoteUri).openStream());
                        if (bitmap != null) {
                            runOnUiThread(() -> image.setImageBitmap(bitmap));
                        }
                    } catch (Exception ignored) {
                    }
                }).start();
            } else {
                image.setImageURI(Uri.parse(uri));
            }
        }
        return frame;
    }

    private String userInitials() {
        String name = getUserName() == null ? "" : getUserName().trim();
        if (name.isEmpty() || "Kullanıcı Adı".equals(name) || "User Name".equals(name)) {
            String email = getUserEmail();
            name = email.contains("@") ? email.substring(0, email.indexOf('@')) : email;
        }
        String[] parts = name.split("[\\s._-]+");
        String first = parts.length > 0 && !parts[0].isEmpty() ? parts[0].substring(0, 1) : "K";
        String last = parts.length > 1 && !parts[parts.length - 1].isEmpty()
                ? parts[parts.length - 1].substring(0, 1) : "";
        return (first + last).toUpperCase(appLocale());
    }

    private String findGoogleProfileUri() {
        try {
            AccountManager manager = AccountManager.get(this);
            for (Account account : manager.getAccountsByType("com.google")) {
                if (!account.name.equalsIgnoreCase(getUserEmail())) {
                    continue;
                }
                String[] keys = {"photo_uri", "picture", "avatar_url"};
                for (String key : keys) {
                    String value = manager.getUserData(account, key);
                    if (value != null && !value.trim().isEmpty()) {
                        return value;
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private void openProfilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_PICK_PROFILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GOOGLE_SIGN_IN) {
            handleGoogleSignInResult(data);
            return;
        }
        if (requestCode == REQUEST_PICK_PROFILE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (SecurityException ignored) {
            }
            getSharedPreferences(PREFS, MODE_PRIVATE)
                    .edit()
                    .putString(profileUriKey(getUserEmail()), uri.toString())
                    .remove(KEY_PROFILE_URI)
                    .apply();
            showScreen(currentScreen);
        }
    }

    private void handleGoogleSignInResult(Intent data) {
        AlertDialog progress = new AlertDialog.Builder(this)
                .setTitle(ui("Google ile giriş", "Google sign-in"))
                .setMessage(ui("Hesap doğrulanıyor...", "Verifying account..."))
                .setCancelable(false)
                .create();
        progress.show();
        final boolean[] finished = {false};
        View anchor = content;
        if (anchor != null) {
            anchor.postDelayed(() -> {
                if (!finished[0] && progress.isShowing()) {
                    Toast.makeText(
                            this,
                            ui("Google girişi beklenenden uzun sürdü. İnternet bağlantını kontrol edip tekrar dene.",
                                    "Google sign-in is taking longer than expected. Check your internet connection and try again."),
                            Toast.LENGTH_LONG
                    ).show();
                    progress.dismiss();
                }
            }, 15000);
        }
        GoogleSignIn.getSignedInAccountFromIntent(data).addOnCompleteListener(task -> {
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account == null || account.getIdToken() == null) {
                    finished[0] = true;
                    if (progress.isShowing()) {
                        progress.dismiss();
                    }
                    Toast.makeText(
                            this,
                            ui("Google hesabı doğrulanamadı. Play imza ayarlarını kontrol edin.",
                                    "Google account could not be verified. Check Play signing settings."),
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }
                if (firebaseAuth == null) {
                    finished[0] = true;
                    if (progress.isShowing()) {
                        progress.dismiss();
                    }
                    Toast.makeText(this, ui("Firebase oturumu hazır değil.", "Firebase session is not ready."), Toast.LENGTH_LONG).show();
                    return;
                }
                firebaseAuth.signInWithCredential(GoogleAuthProvider.getCredential(account.getIdToken(), null))
                        .addOnCompleteListener(authTask -> {
                            finished[0] = true;
                            if (progress.isShowing()) {
                                progress.dismiss();
                            }
                            if (authTask.isSuccessful()) {
                                boolean isNewUser = authTask.getResult() != null
                                        && authTask.getResult().getAdditionalUserInfo() != null
                                        && authTask.getResult().getAdditionalUserInfo().isNewUser();
                                finishFirebaseLogin(firebaseAuth.getCurrentUser());
                                showCountryLanguageSetupAfterGoogle(isNewUser);
                            } else {
                                Toast.makeText(
                                        this,
                                        ui("Google girişi tamamlanamadı: ", "Google sign-in could not be completed: ")
                                                + googleSignInMessage(authTask.getException()),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        });
            } catch (Exception e) {
                finished[0] = true;
                if (progress.isShowing()) {
                    progress.dismiss();
                }
                Toast.makeText(
                        this,
                        ui("Google girişi iptal edildi: ", "Google sign-in was cancelled: ") + googleSignInMessage(e),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void showCountryLanguageSetupAfterGoogle(boolean isNewUser) {
        if (!isNewUser && isCountryPromptCompletedForCurrentUser()) {
            return;
        }
        pendingCountryPromptAfterCloud = true;
        if (cloudLoadInProgress) {
            return;
        }
        if (content == null) {
            ensureCountrySelectionPrompt();
            return;
        }
        content.postDelayed(() -> {
            pendingCountryPromptAfterCloud = false;
            ensureCountrySelectionPrompt();
        }, 250);
    }

    @Override
    public void onBackPressed() {
        handleBackNavigation();
    }

    private void registerSystemBackNavigation() {
        if (Build.VERSION.SDK_INT < 33 || systemBackCallback != null) {
            return;
        }
        systemBackCallback = this::handleBackNavigation;
        getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                systemBackCallback
        );
    }

    private void handleBackNavigation() {
        if (currentScreen == SCREEN_HOME) {
            showExitConfirmation();
            return;
        }
        if (currentScreen == SCREEN_RECORD) {
            editingItem = null;
        }
        if (!screenHistory.isEmpty()) {
            int previous = screenHistory.removeLast();
            while (previous == currentScreen && !screenHistory.isEmpty()) {
                previous = screenHistory.removeLast();
            }
            showScreenFromBack(previous);
            return;
        }
        if (currentScreen == SCREEN_SIGNUP || currentScreen == SCREEN_EMAIL_LOGIN) {
            showScreenFromBack(SCREEN_LOGIN);
        } else if (isLoggedIn()) {
            showScreenFromBack(SCREEN_HOME);
        } else {
            showExitConfirmation();
        }
    }

    private void showScreenFromBack(int screen) {
        suppressScreenHistory = true;
        showScreen(screen);
        suppressScreenHistory = false;
    }

    private void showExitConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle(ui("Uygulamadan Çık", "Exit App"))
                .setMessage(ui("Uygulamadan çıkmak istiyor musun?", "Do you want to exit the app?"))
                .setNegativeButton(ui("Vazgeç", "Cancel"), null)
                .setPositiveButton(ui("Çık", "Exit"), (dialog, which) -> finishAffinity())
                .show();
    }

    private TextView statCard(LinearLayout parent, String label, int color) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding(dp(8), dp(10), dp(8), dp(10));
        card.setBackground(cardBg());
        card.setElevation(dp(2));
        TextView labelView = text(label, 10, COLOR_MUTED, Typeface.BOLD);
        labelView.setGravity(Gravity.CENTER);
        labelView.setSingleLine(false);
        TextView value = text("", 17, color, Typeface.BOLD);
        value.setGravity(Gravity.CENTER);
        value.setSingleLine(true);
        value.setMaxLines(1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            value.setAutoSizeTextTypeUniformWithConfiguration(11, 17, 1, TypedValue.COMPLEX_UNIT_SP);
        }
        value.setPadding(0, dp(6), 0, 0);
        card.addView(labelView);
        card.addView(value);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, dp(86), 1);
        params.setMargins(0, 0, parent.getChildCount() < 2 ? dp(8) : 0, dp(2));
        parent.addView(card, params);
        return value;
    }

    private TextView categoryChip(String category, String amount) {
        TextView chip = text(localizedCategory(category) + "  " + amount, 12, COLOR_PRIMARY, Typeface.BOLD);
        chip.setGravity(Gravity.CENTER);
        chip.setPadding(dp(14), dp(8), dp(14), dp(8));
        chip.setBackground(round(Color.rgb(226, 244, 246), dp(100), 0));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, dp(8), dp(2));
        chip.setLayoutParams(params);
        return chip;
    }

    private TextView sectionTitle(String title) {
        TextView view = text(title, 18, COLOR_TEXT, Typeface.BOLD);
        view.setPadding(0, dp(4), 0, dp(12));
        return view;
    }

    private LinearLayout sectionHeader(String title, String action) {
        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(0, dp(24), 0, dp(12));
        TextView titleView = text(title, 18, COLOR_TEXT, Typeface.BOLD);
        TextView actionView = text(action, 12, COLOR_PRIMARY, Typeface.BOLD);
        header.addView(titleView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        header.addView(actionView);
        if ("Tümünü Gör".equals(action) || "See All".equals(action)) {
            header.setOnClickListener(v -> showScreen(SCREEN_CALENDAR));
        }
        return header;
    }

    private LinearLayout listContainer() {
        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        return list;
    }

    private TextView emptyText(String message) {
        TextView view = text(message, 14, COLOR_MUTED, Typeface.NORMAL);
        view.setPadding(dp(16), dp(16), dp(16), dp(16));
        view.setBackground(cardBg());
        view.setElevation(dp(1));
        return view;
    }

    private EditText field(String hint) {
        EditText editText = new EditText(this);
        editText.setHint(hint);
        editText.setSingleLine(true);
        editText.setTextColor(COLOR_TEXT);
        editText.setHintTextColor(COLOR_MUTED);
        editText.setPadding(dp(12), dp(8), dp(12), dp(8));
        editText.setBackground(round(COLOR_SURFACE, dp(8), COLOR_LINE));
        return editText;
    }

    private TextView text(String value, int sp, int color, int style) {
        TextView textView = new TextView(this);
        textView.setText(value);
        textView.setTextSize(sp);
        textView.setTextColor(color);
        textView.setTypeface(Typeface.DEFAULT, style);
        return textView;
    }

    private Button primaryButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextColor(Color.WHITE);
        button.setTextSize(15);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        button.setAllCaps(false);
        button.setBackground(round(COLOR_PRIMARY_CONTAINER, dp(12), 0));
        return button;
    }

    private Button secondaryButton(String text) {
        Button button = primaryButton(text);
        button.setTextColor(Color.rgb(102, 59, 0));
        button.setBackground(round(COLOR_ACCENT_CONTAINER, dp(12), 0));
        return button;
    }

    private Button outlineButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextSize(15);
        button.setTextColor(COLOR_TEXT);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        button.setAllCaps(false);
        button.setBackground(round(COLOR_SURFACE, dp(12), COLOR_LINE));
        return button;
    }

    private Button smallButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextSize(12);
        button.setTextColor(COLOR_PRIMARY);
        button.setAllCaps(false);
        button.setBackground(round(COLOR_SURFACE_LOW, dp(8), 0));
        return button;
    }

    private GradientDrawable cardBg() {
        return round(COLOR_SURFACE, dp(12), Color.rgb(229, 231, 235));
    }

    private GradientDrawable round(int color, int radius, int strokeColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(radius);
        if (strokeColor != 0) {
            drawable.setStroke(dp(1), strokeColor);
        }
        return drawable;
    }

    private TextView iconBox(String value, int bgColor, int textColor) {
        TextView view = text(value, 17, textColor, Typeface.BOLD);
        view.setGravity(Gravity.CENTER);
        view.setBackground(round(bgColor, dp(10), 0));
        return view;
    }

    private View recordIcon(ExpenseItem item) {
        String service = serviceKey(item.name);
        if (service.isEmpty()) {
            return iconBox(categoryIcon(item.category), categoryTint(item.category), categoryTextColor(item.category));
        }
        return serviceLogo(service, item.category);
    }

    private View serviceLogo(String service, String fallbackCategory) {
        int resourceId = getResources().getIdentifier("logo_" + service, "drawable", getPackageName());
        if (resourceId == 0) {
            return iconBox(categoryIcon(fallbackCategory), categoryTint(fallbackCategory), categoryTextColor(fallbackCategory));
        }
        ImageView logo = new ImageView(this);
        logo.setImageResource(resourceId);
        logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        logo.setAdjustViewBounds(false);
        logo.setBackground(round(Color.WHITE, dp(10), COLOR_LINE));
        logo.setPadding(dp(3), dp(3), dp(3), dp(3));
        return logo;
    }

    private String serviceKey(String name) {
        String value = normalizeServiceName(name);
        if (value.contains("netflix")) return "netflix";
        if (value.contains("spotify")) return "spotify";
        if (value.contains("youtube")) return "youtube";
        if (value.contains("amazon prime") || value.equals("prime") || value.contains("prime video")) return "prime";
        if (value.contains("amazon")) return "amazon";
        if (value.contains("bein") || value.contains("beın")) return "bein";
        if (value.contains("disney")) return "disney";
        if (value.contains("apple tv") || value.contains("appletv")) return "apple";
        if (value.equals("max") || value.contains("hbo max")) return "max";
        if (value.contains("hulu")) return "hulu";
        if (value.contains("paramount")) return "paramount";
        if (value.contains("peacock")) return "peacock";
        if (value.contains("crunchyroll")) return "crunchyroll";
        if (value.equals("now") || value.contains("now tv")) return "now";
        if (value.contains("britbox")) return "britbox";
        if (value.contains("dazn")) return "dazn";
        if (value.equals("wow") || value.contains("wow tv")) return "wow";
        if (value.contains("rtl")) return "rtl";
        if (value.contains("canal")) return "canal";
        if (value.contains("chatgpt") || value.contains("openai")) return "chatgpt";
        if (value.contains("exxen")) return "exxen";
        if (value.contains("tabii") || value.contains("tabi")) return "tabii";
        if (value.contains("tod")) return "tod";
        if (value.contains("gain") || value.contains("gaın")) return "gain";
        return "";
    }

    private String normalizeServiceName(String name) {
        if (name == null) {
            return "";
        }
        return name.toLowerCase(new Locale("tr", "TR"))
                .replace("ı", "i")
                .replace("İ", "i")
                .trim();
    }

    private TextView badge(String value, boolean paid) {
        TextView view = text(paid ? ui("Ödendi", "Paid") : value, 11, paid ? COLOR_PRIMARY : COLOR_ACCENT, Typeface.BOLD);
        view.setGravity(Gravity.CENTER);
        view.setPadding(dp(8), dp(3), dp(8), dp(3));
        view.setBackground(round(paid ? Color.rgb(226, 244, 246) : Color.rgb(255, 236, 211), dp(5), 0));
        return view;
    }

    private String localizedCategory(String category) {
        if ("Abonelik".equals(category)) return ui("Abonelik", "Subscription");
        if ("Elektrik".equals(category)) return ui("Elektrik", "Electricity");
        if ("Su".equals(category)) return ui("Su", "Water");
        if ("Doğalgaz".equals(category)) return ui("Doğalgaz", "Natural Gas");
        if ("İnternet".equals(category)) return ui("İnternet", "Internet");
        if ("Telefon".equals(category)) return ui("Telefon", "Phone");
        if ("Kira".equals(category)) return ui("Kira", "Rent");
        if ("Ulaşım".equals(category)) return ui("Ulaşım", "Transport");
        if ("Market".equals(category)) return ui("Market", "Groceries");
        if ("Diğer".equals(category)) return ui("Diğer", "Other");
        return category;
    }

    private String categoryIcon(String category) {
        if ("Market".equals(category)) return "M";
        if ("Elektrik".equals(category)) return "E";
        if ("Su".equals(category)) return "S";
        if ("Doğalgaz".equals(category)) return "D";
        if ("İnternet".equals(category)) return "İ";
        if ("Telefon".equals(category)) return "T";
        if ("Kira".equals(category)) return "K";
        if ("Ulaşım".equals(category)) return "U";
        if ("Abonelik".equals(category)) return "A";
        return "D";
    }

    private int categoryTint(String category) {
        if ("Elektrik".equals(category)) return Color.rgb(255, 243, 205);
        if ("Market".equals(category)) return Color.rgb(232, 245, 233);
        if ("Su".equals(category)) return Color.rgb(227, 242, 253);
        if ("Doğalgaz".equals(category)) return Color.rgb(255, 236, 219);
        if ("İnternet".equals(category)) return Color.rgb(232, 234, 246);
        if ("Telefon".equals(category)) return Color.rgb(237, 231, 246);
        if ("Kira".equals(category)) return Color.rgb(252, 231, 243);
        if ("Ulaşım".equals(category)) return Color.rgb(224, 247, 250);
        if ("Abonelik".equals(category)) return Color.rgb(218, 242, 244);
        return Color.rgb(238, 240, 242);
    }

    private int categoryTextColor(String category) {
        if ("Elektrik".equals(category)) return Color.rgb(137, 81, 0);
        if ("Market".equals(category)) return Color.rgb(46, 125, 50);
        if ("Su".equals(category)) return Color.rgb(21, 101, 192);
        if ("Doğalgaz".equals(category)) return Color.rgb(173, 83, 24);
        if ("İnternet".equals(category)) return Color.rgb(63, 81, 181);
        if ("Telefon".equals(category)) return Color.rgb(106, 62, 145);
        if ("Kira".equals(category)) return Color.rgb(156, 39, 105);
        if ("Ulaşım".equals(category)) return Color.rgb(0, 105, 112);
        if ("Abonelik".equals(category)) return Color.rgb(0, 102, 111);
        return COLOR_PRIMARY;
    }

    private LinearLayout bottomNav() {
        LinearLayout nav = new LinearLayout(this);
        nav.setOrientation(LinearLayout.HORIZONTAL);
        nav.setGravity(Gravity.CENTER);
        nav.setPadding(dp(12), dp(8), dp(12), dp(8) + systemBottomInset);
        nav.setBackground(round(COLOR_SURFACE, dp(0), Color.rgb(229, 231, 235)));
        nav.setElevation(dp(8));
        nav.addView(navItem(ui("Ana Sayfa", "Home"), SCREEN_HOME, R.drawable.ic_nav_home), new LinearLayout.LayoutParams(0, dp(56), 1));
        nav.addView(navItem(ui("Kategoriler", "Categories"), SCREEN_CATEGORIES, R.drawable.ic_nav_categories), new LinearLayout.LayoutParams(0, dp(56), 1));
        nav.addView(navItem(ui("Takvim", "Calendar"), SCREEN_CALENDAR, R.drawable.ic_nav_calendar), new LinearLayout.LayoutParams(0, dp(56), 1));
        return nav;
    }

    private View navItem(String label, int screen, int iconRes) {
        boolean active = currentScreen == screen;
        int color = active ? COLOR_PRIMARY : COLOR_MUTED;

        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.HORIZONTAL);
        item.setGravity(Gravity.CENTER);
        item.setPadding(dp(10), 0, dp(10), 0);
        item.setBackground(active ? round(Color.rgb(226, 244, 246), dp(18), 0) : null);
        item.setOnClickListener(v -> showScreen(screen));

        ImageView icon = new ImageView(this);
        icon.setImageResource(iconRes);
        Drawable drawable = icon.getDrawable();
        if (drawable != null) {
            drawable.setTint(color);
        }
        icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(dp(18), dp(18));
        iconParams.setMargins(0, 0, dp(7), 0);
        item.addView(icon, iconParams);

        TextView labelView = text(label, 12, color, Typeface.BOLD);
        labelView.setGravity(Gravity.CENTER);
        labelView.setSingleLine(true);
        item.addView(labelView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return item;
    }

    private void refreshBottomNav() {
        if (bottomNav == null) {
            return;
        }
        bottomNav.removeAllViews();
        bottomNav.addView(navItem(ui("Ana Sayfa", "Home"), SCREEN_HOME, R.drawable.ic_nav_home), new LinearLayout.LayoutParams(0, dp(56), 1));
        bottomNav.addView(navItem(ui("Kategoriler", "Categories"), SCREEN_CATEGORIES, R.drawable.ic_nav_categories), new LinearLayout.LayoutParams(0, dp(56), 1));
        bottomNav.addView(navItem(ui("Takvim", "Calendar"), SCREEN_CALENDAR, R.drawable.ic_nav_calendar), new LinearLayout.LayoutParams(0, dp(56), 1));
    }

    private FrameLayout.LayoutParams bottomNavParams() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                bottomNavHeight(),
                Gravity.BOTTOM
        );
        params.setMargins(0, 0, 0, 0);
        return params;
    }

    private void updateBottomNavInsets() {
        if (bottomNav == null) {
            return;
        }
        bottomNav.setPadding(dp(12), dp(8), dp(12), dp(8) + systemBottomInset);
        ViewGroup.LayoutParams params = bottomNav.getLayoutParams();
        if (params != null) {
            params.height = bottomNavHeight();
            bottomNav.setLayoutParams(params);
        }
    }

    private int bottomNavHeight() {
        return dp(72) + systemBottomInset;
    }

    private int bottomContentPadding(boolean includesBottomNav) {
        return includesBottomNav ? bottomNavHeight() + dp(24) : dp(96);
    }

    private int categoryIndex(String category) {
        for (int i = 0; i < CATEGORIES.length; i++) {
            if (CATEGORIES[i].equals(category)) {
                return i;
            }
        }
        return 0;
    }

    private boolean isRecurringCategory(String category) {
        return "Abonelik".equals(category)
                || "Elektrik".equals(category)
                || "Su".equals(category)
                || "Doğalgaz".equals(category)
                || "İnternet".equals(category)
                || "Telefon".equals(category)
                || "Kira".equals(category);
    }

    private void updateDateLabel(TextView label, Calendar selected) {
        label.setText(ui("Ödeme tarihi: ", "Payment date: ") + formatDate(selected) + ui("  (değiştir)", "  (change)"));
    }

    private String formatDate(Calendar date) {
        return new SimpleDateFormat("dd MMMM yyyy", appLocale()).format(date.getTime());
    }

    private String shortDate(Calendar date) {
        return new SimpleDateFormat("dd MMM", appLocale()).format(date.getTime());
    }

    private long daysBetween(Calendar start, Calendar end) {
        Calendar s = (Calendar) start.clone();
        Calendar e = (Calendar) end.clone();
        s.set(Calendar.HOUR_OF_DAY, 0);
        s.set(Calendar.MINUTE, 0);
        s.set(Calendar.SECOND, 0);
        s.set(Calendar.MILLISECOND, 0);
        e.set(Calendar.HOUR_OF_DAY, 0);
        e.set(Calendar.MINUTE, 0);
        e.set(Calendar.SECOND, 0);
        e.set(Calendar.MILLISECOND, 0);
        return (e.getTimeInMillis() - s.getTimeInMillis()) / (24L * 60L * 60L * 1000L);
    }

    private String strongestPriceIncrease() {
        String best = "";
        double bestPercent = 0;
        for (ExpenseItem item : items) {
            if (item.actualPayments.size() < 2) {
                continue;
            }
            List<String> keys = new ArrayList<>(item.actualPayments.keySet());
            Collections.sort(keys, (a, b) -> monthIndex(a) - monthIndex(b));
            String prevKey = keys.get(keys.size() - 2);
            String lastKey = keys.get(keys.size() - 1);
            double previous = item.actualPayments.get(prevKey);
            double latest = item.actualPayments.get(lastKey);
            if (previous <= 0 || latest <= previous) {
                continue;
            }
            double percent = ((latest - previous) / previous) * 100;
            if (percent > bestPercent) {
                bestPercent = percent;
                best = item.name + ui(" son ödemede %", " increased by %") + Math.round(percent) + ui(" arttı.", " in the last payment.");
            }
        }
        return best;
    }

    private int monthIndex(String key) {
        String[] parts = key.split("-");
        if (parts.length != 2) {
            return 0;
        }
        try {
            return Integer.parseInt(parts[0]) * 12 + Integer.parseInt(parts[1]);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private String nearestEndingSubscription() {
        Calendar now = Calendar.getInstance();
        ExpenseItem nearest = null;
        long nearestDays = Long.MAX_VALUE;
        for (ExpenseItem item : items) {
            if (!item.hasEndDate) {
                continue;
            }
            long days = daysBetween(now, item.endDate());
            if (days >= 0 && days < nearestDays) {
                nearestDays = days;
                nearest = item;
            }
        }
        if (nearest == null || nearestDays > 60) {
            return "";
        }
        return nearest.name + ui(" tarifesi ", " plan ends in ") + nearestDays + ui(" gün içinde bitiyor.", " days.");
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private static class ExpenseItem {
        long id;
        String name;
        double amount;
        String category;
        boolean monthly;
        boolean paid;
        int day;
        int month;
        int year;
        int paidMonth = -1;
        int paidYear = -1;
        boolean reminderEnabled = true;
        String reminderFrequency = "Günlük";
        boolean hasEndDate;
        int endDay = 1;
        int endMonth;
        int endYear;
        final Map<String, Double> actualPayments = new HashMap<>();

        static ExpenseItem create(String name, double amount, String category, boolean monthly, Calendar date, boolean paid) {
            ExpenseItem item = new ExpenseItem();
            item.id = System.currentTimeMillis() + name.hashCode();
            item.name = name;
            item.amount = amount;
            item.category = category;
            item.monthly = monthly;
            item.paid = paid;
            item.day = date.get(Calendar.DAY_OF_MONTH);
            item.month = date.get(Calendar.MONTH);
            item.year = date.get(Calendar.YEAR);
            if (paid) {
                item.paidMonth = date.get(Calendar.MONTH);
                item.paidYear = date.get(Calendar.YEAR);
            }
            return item;
        }

        boolean isPaidFor(int targetYear, int targetMonth) {
            if (actualPayments.containsKey(monthKey(targetYear, targetMonth))) {
                return true;
            }
            if (!paid) {
                return false;
            }
            if (!monthly) {
                return true;
            }
            return paidYear == targetYear && paidMonth == targetMonth;
        }

        boolean hasVariableMonthlyAmount() {
            return "Elektrik".equals(category) || "Su".equals(category) || "Doğalgaz".equals(category);
        }

        boolean isEstimatedFor(int targetYear, int targetMonth) {
            return hasVariableMonthlyAmount()
                    && !actualPayments.isEmpty()
                    && !actualPayments.containsKey(monthKey(targetYear, targetMonth));
        }

        double amountForMonth(int targetYear, int targetMonth) {
            Double actual = actualPayments.get(monthKey(targetYear, targetMonth));
            if (actual != null) {
                return actual;
            }
            if (!hasVariableMonthlyAmount()) {
                return amount;
            }
            int targetIndex = targetYear * 12 + targetMonth;
            double total = 0;
            int count = 0;
            for (Map.Entry<String, Double> entry : actualPayments.entrySet()) {
                String[] parts = entry.getKey().split("-");
                if (parts.length != 2) continue;
                try {
                    int index = Integer.parseInt(parts[0]) * 12 + Integer.parseInt(parts[1]);
                    if (index < targetIndex) {
                        total += entry.getValue();
                        count++;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
            return count == 0 ? amount : total / count;
        }

        void setActualPayment(int targetYear, int targetMonth, double actual) {
            actualPayments.put(monthKey(targetYear, targetMonth), actual);
        }

        void removeActualPayment(int targetYear, int targetMonth) {
            actualPayments.remove(monthKey(targetYear, targetMonth));
        }

        boolean isFinalMonth(Calendar date) {
            return hasEndDate && date.get(Calendar.YEAR) == endYear && date.get(Calendar.MONTH) == endMonth;
        }

        Calendar endDate() {
            Calendar date = Calendar.getInstance();
            date.set(endYear, endMonth, endDay, 0, 0, 0);
            date.set(Calendar.MILLISECOND, 0);
            return date;
        }

        private static String monthKey(int targetYear, int targetMonth) {
            return targetYear + "-" + targetMonth;
        }

        Calendar dueDateForMonth(Calendar now) {
            Calendar date = Calendar.getInstance();
            date.set(Calendar.YEAR, year);
            date.set(Calendar.MONTH, month);
            date.set(Calendar.DAY_OF_MONTH, Math.min(day, date.getActualMaximum(Calendar.DAY_OF_MONTH)));
            date.set(Calendar.HOUR_OF_DAY, 9);
            date.set(Calendar.MINUTE, 0);
            date.set(Calendar.SECOND, 0);
            date.set(Calendar.MILLISECOND, 0);
            if (!monthly) {
                return date;
            }
            date.set(Calendar.YEAR, now.get(Calendar.YEAR));
            date.set(Calendar.MONTH, now.get(Calendar.MONTH));
            date.set(Calendar.DAY_OF_MONTH, Math.min(day, date.getActualMaximum(Calendar.DAY_OF_MONTH)));
            return date;
        }

        Calendar nextReminderDate(Calendar now) {
            Calendar date = dueDateForMonth(now);
            if (!monthly) {
                return date;
            }
            if (date.before(now)) {
                date.add(Calendar.MONTH, 1);
                date.set(Calendar.DAY_OF_MONTH, Math.min(day, date.getActualMaximum(Calendar.DAY_OF_MONTH)));
            }
            return date;
        }

        JSONObject toJson() {
            JSONObject object = new JSONObject();
            try {
                object.put("id", id);
                object.put("name", name);
                object.put("amount", amount);
                object.put("category", category);
                object.put("monthly", monthly);
                object.put("paid", paid);
                object.put("day", day);
                object.put("month", month);
                object.put("year", year);
                object.put("paidMonth", paidMonth);
                object.put("paidYear", paidYear);
                object.put("reminderEnabled", reminderEnabled);
                object.put("reminderFrequency", reminderFrequency);
                object.put("hasEndDate", hasEndDate);
                object.put("endDay", endDay);
                object.put("endMonth", endMonth);
                object.put("endYear", endYear);
                JSONObject actuals = new JSONObject();
                for (Map.Entry<String, Double> entry : actualPayments.entrySet()) {
                    actuals.put(entry.getKey(), entry.getValue());
                }
                object.put("actualPayments", actuals);
            } catch (Exception ignored) {
            }
            return object;
        }

        static ExpenseItem fromJson(JSONObject object) {
            ExpenseItem item = new ExpenseItem();
            item.id = object.optLong("id", System.currentTimeMillis());
            item.name = object.optString("name", "Gider");
            item.amount = object.optDouble("amount", 0);
            item.category = object.optString("category", "Diğer");
            item.monthly = object.optBoolean("monthly", false);
            item.paid = object.optBoolean("paid", false);
            item.day = object.optInt("day", 1);
            item.month = object.optInt("month", 0);
            item.year = object.optInt("year", Calendar.getInstance().get(Calendar.YEAR));
            item.paidMonth = object.optInt("paidMonth", item.paid ? item.month : -1);
            item.paidYear = object.optInt("paidYear", item.paid ? item.year : -1);
            item.reminderEnabled = object.optBoolean("reminderEnabled", true);
            item.reminderFrequency = object.optString("reminderFrequency", "Günlük");
            item.hasEndDate = object.optBoolean("hasEndDate", false);
            item.endDay = object.optInt("endDay", item.day);
            item.endMonth = object.optInt("endMonth", item.month);
            item.endYear = object.optInt("endYear", item.year);
            JSONObject actuals = object.optJSONObject("actualPayments");
            if (actuals != null) {
                java.util.Iterator<String> keys = actuals.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    item.actualPayments.put(key, actuals.optDouble(key, 0));
                }
            }
            return item;
        }
    }
}
