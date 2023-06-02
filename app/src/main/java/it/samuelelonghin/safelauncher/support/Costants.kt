package it.samuelelonghin.safelauncher.support

import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.list.ListActivity
import it.samuelelonghin.safelauncher.settings.AuthActivity
import it.samuelelonghin.safelauncher.settings.SettingsActivity


const val PREF_VERSION = "version"
const val DEFAULT_INDEX = 1000
const val SETTINGS_AUTH_LENGTH = 6


/**
 * BROADCASTS
 */
const val BROADCAST_NOTIFICATIONS = "it.samuelelonghin.safelauncher.notifications"

/**
 * THEME
 */
const val PREF_DOMINANT = "custom_dominant"
const val PREF_VIBRANT = "custom_vibrant"
const val PREF_WALLPAPER = "background_uri"
const val PREF_THEME = "theme"

const val PREF_SCREEN_TIMEOUT_DISABLED = "disableTimeout"
const val PREF_SCREEN_FULLSCREEN = "useFullScreen"
const val PREF_DATE_FORMAT = "dateFormat"


/**
 *  REQUEST CODES
 */
const val REQUEST_PICK_IMAGE = 1
const val REQUEST_CHOOSE_APP = 2
const val REQUEST_UNINSTALL = 3
const val REQUEST_PERMISSION_STORAGE = 4
const val REQUEST_AUTH_FOR_SETTINGS = 5
const val REQUEST_PERMISSION_NOTIFICATION_POLICY = 6

/**
 * SHARED PREFERENCES
 */
const val IS_TUTORIAL_FINISHED = "is-tutorial-finished"
const val WIDGET_NUMBER_COLUMNS = "widget-number-columns"
const val WIDGET_NUMBER_ROWS = "widget-number-rows"
const val WIDGET_SHOW_LABELS = "widget-show-labels"
const val WIDGET_IS_SCROLLABLE = "widget-is-scrollable"
const val WIDGET_FORCE_SETTINGS = "widget-force-settings"
const val WIDGET_FORCE_APPS = "widget-force-apps"
const val CONTACTS_NUMBER_COLUMNS = "contacts-number-columns"
const val CONTACTS_IS_SCROLLABLE = "contacts-is-scrollable"
const val WIDGETS_LIST = "widgets-list"
const val VIEW_CONTACT_SHOW_RAPID_CALL = "view-contact-show-rapid-call"
const val VIEW_CONTACT_SHOW_RAPID_CHAT = "view-contact-show-rapid-chat"
const val VIEW_CONTACT_SHOW_NOTIFICATIONS = "view-contact-show-notifications"
const val VIEW_CONTACT_RAPID_CHAT_APP = "view-contact-rapid-chat-app"
const val DRAWER_SEARCH_AT_LAUNCH = "drawer-search-at-launch"
const val OPEN_APP_ON_ONLY_ONE_RESULTS = "open-app-on-only-one-results"
const val OPEN_KEYBOARD_ON_SEARCH = "open-keyboard-on-search"
const val DATE_FORMAT = "date-format"
const val TIME_FORMAT = "time-format"
const val VIEW_CONTACT_BUTTONS_DIRECTION = "view-contact-buttons-direction"
const val APPS_LIST_VIEW_TYPE = "apps-list-view-type"
const val SETTINGS_REQUIRES_AUTH = "settings-requires-auth"
const val SETTINGS_FORCE_FULL_SCREEN = "settings-force-full-screen"
const val SETTINGS_AUTH = "settings-auth"

/**
 * DEFAULT PREFERENCES
 */

const val CONTACTS_NUMBER_COLUMNS_PREF = 3
const val WIDGET_NUMBER_COLUMNS_PREF = 5
const val WIDGET_NUMBER_ROWS_PREF = 2
const val WIDGET_SHOW_LABELS_PREF = false
const val CONTACTS_IS_SCROLLABLE_PREF = true
const val WIDGET_IS_SCROLLABLE_PREF = true
const val WIDGET_FORCE_SETTINGS_DEF = true
const val WIDGET_FORCE_APPS_DEF = true
const val VIEW_CONTACT_SHOW_RAPID_CALL_PREF = true
const val VIEW_CONTACT_SHOW_RAPID_CHAT_PREF = true
const val VIEW_CONTACT_RAPID_CHAT_APP_PREF = "WhatsApp"
const val VIEW_CONTACT_SHOW_NOTIFICATIONS_PREF = true
const val DRAWER_SEARCH_AT_LAUNCH_PREF = true
const val VIEW_CONTACT_BUTTONS_DIRECTION_PREF = false
const val APPS_LIST_VIEW_TYPE_PREF = 0
const val SETTINGS_REQUIRES_AUTH_DEF = false
const val SETTINGS_FORCE_FULL_SCREEN_DEF = false
const val SETTINGS_AUTH_DEF = ""
const val OPEN_KEYBOARD_ON_SEARCH_DEF = true


/**
 * PREFERENCES
 */

val WIDGETS_PREFERENCES = setOf(
    WIDGET_NUMBER_ROWS, WIDGET_NUMBER_COLUMNS, WIDGET_FORCE_APPS,
    WIDGET_FORCE_SETTINGS, WIDGET_SHOW_LABELS, WIDGET_IS_SCROLLABLE
)

val CONTACTS_PREFERENCES = setOf(
    CONTACTS_IS_SCROLLABLE, CONTACTS_NUMBER_COLUMNS
)

/**
 * ACTIVITIES
 */
const val ACTIVITY_APPS = "activity_apps"
const val ACTIVITY_AUTH = "activity_auth"
const val ACTIVITY_SETTINGS = "activity_settings"
const val ACTIVITY_PICK = "activity_pick"

/**
 * ACTIONS
 */
const val ACTION_MUTE = "action_notifications"
const val ACTION_FLASH = "action_flash"
const val ACTION_ADD_WIDGET = "action_add_widget"


val ACTIVITY_TO_CLASS: Map<String, Class<*>> = mapOf(
    ACTIVITY_SETTINGS to SettingsActivity::class.java,
    ACTIVITY_AUTH to AuthActivity::class.java,
    ACTIVITY_APPS to ListActivity::class.java,
    ACTIVITY_PICK to ListActivity::class.java,
).withDefault { SettingsActivity::class.java }

val ACTIVITY_TO_NAME: Map<String, String> = mapOf(
    ACTIVITY_SETTINGS to "Settings",
    ACTIVITY_APPS to "APPS",
    ACTIVITY_AUTH to "Settings",
    ACTIVITY_PICK to "Scegli"
).withDefault { "Errore" }

val ACTIVITY_TO_RESOURCE_ICON: Map<String, Int> = mapOf(
    ACTIVITY_SETTINGS to R.drawable.ic_baseline_settings_24,
    ACTIVITY_AUTH to R.drawable.ic_baseline_settings_24,
    ACTIVITY_APPS to R.drawable.ic_baseline_apps_24,
    ACTIVITY_PICK to R.drawable.ic_baseline_add_240
).withDefault { R.drawable.ic_baseline_error_24 }

val ACTION_TO_RESOURCE_ICON: Map<String, List<Int>> = mapOf(
    ACTION_MUTE to listOf(
        R.drawable.ic_baseline_notifications_active_24,
        R.drawable.ic_baseline_notifications_off_24
    ),
    ACTION_FLASH to listOf(
        R.drawable.ic_baseline_flashlight_on_24,
        R.drawable.ic_baseline_flashlight_off_24
    )
).withDefault { listOf(R.drawable.ic_baseline_error_24) }

val ACTION_TO_NAME: Map<String, List<String>> = mapOf(
    ACTION_MUTE to listOf(
        "Mute",
        "UnMute"
    ),
    ACTION_FLASH to listOf(
        "Light",
        "Light Off"
    )
).withDefault { listOf("Error") }

/**
 * View User
 */

val VIEW_CONTACT_RAPID_APP_URL: Map<String, String> = mapOf(
    "WhatsApp" to "https://api.whatsapp.com/send?phone=%s",
    "Telegram" to "tg://resolve?to=%s",
    "Default" to "smsto:%s"
)

val VIEW_CONTACT_RAPID_APP_TO_INDEX: Map<String, Int> =
    mapOf("Default" to 0, "WhatsApp" to 1, "Telegram" to 2).withDefault { 0 }

val VIEW_CONTACT_RAPID_INDEX_TO_APP: Map<Int, String> =
    mapOf(0 to "Default", 1 to "WhatsApp", 2 to "Telegram").withDefault { "Default" }
