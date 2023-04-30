package it.samuelelonghin.safelauncher.support


const val PREF_VERSION = "version"


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

const val PREF_DOUBLE_ACTIONS_ENABLED = "enableDoubleActions"
const val PREF_SEARCH_AUTO_LAUNCH = "searchAutoLaunch"
const val PREF_SEARCH_AUTO_KEYBOARD = "searchAutoKeyboard"

const val PREF_SLIDE_SENSITIVITY = "slideSensitivity"

const val PREF_STARTED = "startedBefore"
const val PREF_STARTED_TIME = "firstStartup"


/**
 *
 */
const val PERMISSIONS_REQUEST_READ_CONTACTS = 100


/**
 *  REQUEST CODES
 */
const val REQUEST_PICK_IMAGE = 1
const val REQUEST_CHOOSE_APP = 2
const val REQUEST_UNINSTALL = 3
const val REQUEST_PERMISSION_STORAGE = 4

/**
 * SHARED PREFERENCES
 */
const val IS_TUTORIAL_FINISHED = "is-tutorial-finished"
const val WIDGET_NUMBER_COLUMNS = "widget-number-columns"
const val WIDGET_NUMBER_ROWS = "widget-number-rows"
const val CONTACTS_NUMBER_COLUMNS = "contacts-number-columns"
const val CONTACTS_IS_SCROLLABLE = "contacts-is-scrollable"
const val WIDGETS_LIST = "widgets-list"
const val VIEW_CONTACT_SHOW_RAPID_CALL = "view-contact-show-rapid-call"
const val VIEW_CONTACT_SHOW_RAPID_CHAT = "view-contact-show-rapid-chat"
const val VIEW_CONTACT_SHOW_NOTIFICATIONS = "view-contact-show-notifications"
const val VIEW_CONTACT_RAPID_CHAT_APP = "view-contact-rapid-chat-app"
const val DRAWER_SEARCH_AT_LAUNCH = "drawer-search-at-launch"


/**
 * DEFAULT PREFERENCES
 */

const val CONTACTS_NUMBER_COLUMNS_PREF = 3
const val WIDGET_NUMBER_COLUMNS_PREF = 5
const val WIDGET_NUMBER_ROWS_PREF = 2
const val CONTACTS_IS_SCROLLABLE_PREF = true
const val VIEW_CONTACT_SHOW_RAPID_CALL_PREF = true
const val VIEW_CONTACT_SHOW_RAPID_CHAT_PREF = true
const val VIEW_CONTACT_RAPID_CHAT_APP_PREF = "WhatsApp"
const val VIEW_CONTACT_SHOW_NOTIFICATIONS_PREF = true
const val DRAWER_SEARCH_AT_LAUNCH_PREF = true


/**
 * View User
 */

val VIEW_CONTACT_RAPID_APP_URL: Map<String, String> =
    mapOf(
        "WhatsApp" to "https://api.whatsapp.com/send?phone=%s",
        "Telegram" to "tg://resolve?to=%s",
        "Default" to "smsto:%s"
    )

val VIEW_CONTACT_RAPID_APP_TO_INDEX: Map<String, Int> =
    mapOf("Default" to 0, "WhatsApp" to 1, "Telegram" to 2).withDefault { 0 }

val VIEW_CONTACT_RAPID_INDEX_TO_APP: Map<Int, String> =
    mapOf(0 to "Default", 1 to "WhatsApp", 2 to "Telegram").withDefault { "Default" }
