package com.marcoscherzer.minigui.appstyle;

import static android.view.View.TEXT_ALIGNMENT_TEXT_START;
import static com.marcoscherzer.minigui.lib.msimplegui.style.MStyleUtil.derive;

import android.graphics.Color;

import com.marcoscherzer.minigui.lib.msimplegui.style.MStyleUtil;

/**
 * Stylesheet Author/Copyright Marco Scherzer, All Rights Reserved
 */
public class MStyleRoot {

    // Basisfarben (überschrieben)
    public static final int BASE = MStyleUtil.rgb(255, 150, 0);//Color.rgb(177,150,0);
    public static final int COLOR = BASE;
    public static final int TEXT_BASE_COLOR = MStyleUtil.ladder(COLOR, 45, 46, 59, 60);
    public static final int BACKGROUND = MStyleUtil.rgb(2, 2, 8);
    public static final int CONTROL_INNER_BACKGROUND = MStyleUtil.rgb(6, 6, 14);
    public static final int CONTROL_INNER_BACKGROUND_ALT = MStyleUtil.rgb(4, 4, 12);

    // Textfarben (überschrieben)
    public static final int CONTROL_SHADOW = MStyleUtil.rgb(4, 4, 12);
    public static final int TEXT_DARK_COLOR = MStyleUtil.rgb(80, 200, 255);//-fx-mid-text-color
    public static final int TEXT_MID_COLOR = Color.rgb(0, 160, 200);//MStyleUtil.rgba(90, 210, 255,0.7f);
    public static final int TEXT_LIGHT_COLOR = Color.rgb(0, 160, 200);//MStyleUtil.rgba(60, 160, 230,0.7f);
    // Fokus & Auswahl
    public static final int ACCENT = MStyleUtil.rgb(255, 80, 0);
    public static final int DEFAULT_BUTTON = MStyleUtil.rgb(255, 110, 30);
    public static final int FOCUS_COLOR = MStyleUtil.rgb(255, 100, 0);
    public static final int FAINT_FOCUS_COLOR = MStyleUtil.rgba(255, 100, 0, 0.3f);
    // Z1 und Z2 sind visuelle Effekte, nicht direkt als Farbe darstellbar
    // Schatten & Elevation
    public static final int ELEVATION_SHADOW_COLOR = MStyleUtil.rgba(255, 100, 0, 0.45f);
    // Flat-Button-Zustände
    public static final int FLAT_BUTTON_HOVER_BACKGROUND = MStyleUtil.rgba(255, 80, 0, 0.3f);
    public static final int FLAT_BUTTON_PRESSED_BACKGROUND = MStyleUtil.rgba(255, 80, 0, 0.5f);
    public static final int FLAT_BUTTON_DISABLED_FILL = MStyleUtil.rgba(255, 80, 0, 0.12f);
    // Abgeleitete Farben
    public static final int HOVER_BASE = MStyleUtil.ladder(BASE, 20, 35, 50);
    public static final int PRESSED_BASE = MStyleUtil.derive(BASE, -6);
    public static final int TEXT_INNER_COLOR = MStyleUtil.ladder(CONTROL_INNER_BACKGROUND, 45, 46, 59, 60);
    public static final int FOCUSED_TEXT_BASE_COLOR = MStyleUtil.ladder(ACCENT, 45, 46, 59, 60);
    public static final int FOCUSED_MARK_COLOR = FOCUSED_TEXT_BASE_COLOR;
    public static final int MSECTIONSGRID_BACKGROUND_COLOR_EVEN = Color.rgb(27, 27, 27);//BASE;
    public static final int MSECTIONSGRID_BACKGROUND_COLOR_ODD = derive(MSECTIONSGRID_BACKGROUND_COLOR_EVEN, 10);//derive(BASE, 10) ;
    public static final int MSECTION_LABEL_TEXT_ALIGNMENT = TEXT_ALIGNMENT_TEXT_START;


    //----------------------------------------------------------------------
    public static final int MSECTION_DATALABEL_TEXT_ALIGNMENT = TEXT_ALIGNMENT_TEXT_START;
    public static final int MSECTION_BACKGROUND_COLOR = 0x00000000; // transparent;
    public static final int MSECTION_LABEL_FONT_SIZE = 14;
    public static final int MSECTION_LABEL_COLOR = 0x00000000;
    //-fx-text-fill: -fx-mid-text-color;
    public static final int MSECTION_LABEL_FONT_COLOR = TEXT_MID_COLOR;//TEXT_LIGHT_COLOR;//MStyleUtil.rgb(0, 150, 225);
    public static final int MSECTION_DATALABEL_FONT_SIZE = 25; //-fx-font-size: 14pt;

    //-fx-font-size: 14pt;
    //-fx-text-fill: -fx-light-text-color;
    public static final int MSECTION_DATALABEL_FONT_COLOR = TEXT_LIGHT_COLOR;
    public static final int MSECTION_DATALABEL_COLOR = 0x00000000; // transparent;
    public static final boolean MGRID_CELLS_SHOW_TOP_BORDER = true;


    //----------------------------------------------------------------------
    public static final boolean MGRID_CELLS_SHOW_LEFT_BORDER = true;
    public static final boolean MGRID_CELLS_SHOW_BOTTOM_BORDER = true;
    public static final boolean MGRID_CELLS_SHOW_RIGHT_BORDER = true;
    public static final int MGRID_CELLS_FILL_COLOR = 0x00000000; // transparent
    public static final int MGRID_CELLS_RIPPLE_COLOR = 0x00000000; // transparent
    public static final float MGRID_CELLS_CORNER_RADIUS_DP = 0f;
    public static final int MGRID_CELLS_STROKE_COLOR = 0x00000000; // transparent
    public static final float MGRID_CELLS_STROKE_WIDTH_DP = 0.0f;
    // === TopAppBar / Toolbar ===
    public final int TOOLBAR_BACKGROUND_COLOR = BACKGROUND;


    //----------------------------Default Components--------------------------------------------------------------------------
    public final int TOOLBAR_TITLE_COLOR = ACCENT;
    public final int TOOLBAR_SUBTITLE_COLOR = ACCENT;
    public final float TOOLBAR_ELEVATION = 6f;
    // === BottomNavigationView ===
    public final int BOTTOM_NAVIGATION_BACKGROUND_COLOR = BASE;
    public final int BOTTOM_NAVIGATION_ICON_COLOR = TEXT_LIGHT_COLOR;
    public final int BOTTOM_NAVIGATION_TEXT_COLOR = TEXT_MID_COLOR;
    public final float BOTTOM_NAVIGATION_ELEVATION = 6f;
    // === NavigationDrawer (NavigationView) ===
    public final int NAVIGATION_DRAWER_BACKGROUND_COLOR = CONTROL_INNER_BACKGROUND_ALT;
    public final int NAVIGATION_DRAWER_ICON_COLOR = TEXT_LIGHT_COLOR;
    public final int NAVIGATION_DRAWER_TEXT_COLOR = TEXT_MID_COLOR;
    public final float NAVIGATION_DRAWER_ELEVATION = 8f;
    // === NavigationRailView ===
    public final int NAVIGATION_RAIL_BACKGROUND_COLOR = CONTROL_INNER_BACKGROUND;
    public final int NAVIGATION_RAIL_ICON_COLOR = TEXT_LIGHT_COLOR;
    public final int NAVIGATION_RAIL_TEXT_COLOR = TEXT_MID_COLOR;
    public final float NAVIGATION_RAIL_ELEVATION = 8f;
    // === TabLayout ===
    public final int TABLAYOUT_BACKGROUND_COLOR = BASE;
    public final int TABLAYOUT_TEXT_COLOR = TEXT_LIGHT_COLOR;
    public final int TABLAYOUT_TEXT_COLOR_SELECTED = TEXT_MID_COLOR;
    public final int TABLAYOUT_INDICATOR_COLOR = ACCENT;
    public final float TABLAYOUT_ELEVATION = 4f;
    // === BottomAppBar ===
    public final int BOTTOM_APP_BAR_BACKGROUND_COLOR = BASE;
    public final float BOTTOM_APP_BAR_ELEVATION = 6f;
    // === SearchBar ===
    public final int SEARCH_BAR_BACKGROUND_COLOR = CONTROL_INNER_BACKGROUND;
    public final float SEARCH_BAR_ELEVATION = 4f;
    public final int SEARCH_BAR_STROKE_COLOR = TEXT_MID_COLOR;
    //--------------------------------------------------------------------------------------------
    // === Badge ===
    public final int BADGE_BACKGROUND_COLOR = ACCENT;
    public final int BADGE_TEXT_COLOR = TEXT_MID_COLOR;
    // === ExtendedFloatingActionButton ===
    public final int EXTENDED_FAB_BACKGROUND_COLOR = ACCENT;
    public final int EXTENDED_FAB_TEXT_COLOR = TEXT_MID_COLOR;
    public final int EXTENDED_FAB_RIPPLE_COLOR = FAINT_FOCUS_COLOR;
    public final float EXTENDED_FAB_ELEVATION = 6f;
    // === FloatingActionButton ===
    public final int FAB_BACKGROUND_COLOR = ACCENT;
    public final int FAB_RIPPLE_COLOR = FAINT_FOCUS_COLOR;
    public final float FAB_ELEVATION = 6f;
    // === Slider ===
    public final int SLIDER_TRACK_COLOR = ACCENT;
    public final int SLIDER_THUMB_COLOR = TEXT_MID_COLOR;
    public final int SLIDER_HALO_COLOR = FAINT_FOCUS_COLOR;
    // === Snackbar ===
    public final int SNACKBAR_BACKGROUND_COLOR = BASE;
    public final int SNACKBAR_TEXT_COLOR = TEXT_MID_COLOR;
    public final int SNACKBAR_ACTION_COLOR = ACCENT;
    // === TextInputLayout ===
    public final int TEXTINPUT_BACKGROUND_COLOR = CONTROL_INNER_BACKGROUND_ALT;
    public final int TEXTINPUT_HINT_COLOR = TEXT_LIGHT_COLOR;
    public final int TEXTINPUT_HELPER_COLOR = TEXT_MID_COLOR;
    public final int TEXTINPUT_ERROR_COLOR = ACCENT;
    // === DROPDOWN ===
    public final int DROPDOWN_BACKGROUND_COLOR = CONTROL_INNER_BACKGROUND_ALT;
    public final int DROPDOWN_TEXT_COLOR = TEXT_MID_COLOR;
    public final int DROPDOWN_HINT_COLOR = TEXT_LIGHT_COLOR;
    // === Button ===
    public final int BUTTON_TEXT_COLOR = TEXT_MID_COLOR;
    //--------------------------------------------------------------------------------------------
    public final float BUTTON_TEXT_SIZE = 16f;
    public final int BUTTON_BACKGROUND_COLOR = BASE;
    public final int BUTTON_SHADOW_COLOR = CONTROL_SHADOW;
    public final float BUTTON_SHADOW_RADIUS = 7f;
    public final float BUTTON_SHADOW_DX = 0f;
    public final float BUTTON_SHADOW_DY = 0f;
    public final int BUTTON_COMPOUND_DRAWABLE_PADDING = 8;
    public final int BUTTON_HOVER = MStyleUtil.argb(77, 255, 80, 0);
    // === DrawerLayout ===
    public final int DRAWER_LAYOUT_BACKGROUND_COLOR = CONTROL_INNER_BACKGROUND;
    public final float DRAWER_LAYOUT_ELEVATION = 8f;
    // === DropdownMenu (AutoCompleteTextView) ===
    public final int DROPDOWN_MENU_BACKGROUND_COLOR = CONTROL_INNER_BACKGROUND_ALT;
    public final int DROPDOWN_MENU_TEXT_COLOR = TEXT_MID_COLOR;
    public final float DROPDOWN_MENU_TEXT_SIZE = 14f;
    // === FragmentContainerView ===
    public final int FRAGMENT_CONTAINER_BACKGROUND_COLOR = CONTROL_INNER_BACKGROUND;
    public final float FRAGMENT_CONTAINER_ELEVATION = 6f;
    // === NavigationBar (Material 3) ===
    public final int NAVIGATION_BAR_BACKGROUND_COLOR = BASE;
    public final int NAVIGATION_BAR_ICON_COLOR = TEXT_LIGHT_COLOR;
    public final int NAVIGATION_BAR_TEXT_COLOR = TEXT_MID_COLOR;
    public final float NAVIGATION_BAR_ELEVATION = 6f;
    // === SideSheet ===
    public final int SIDE_SHEET_BACKGROUND_COLOR = CONTROL_INNER_BACKGROUND_ALT;
    public final float SIDE_SHEET_ELEVATION = 6f;
    // === TopAppBar (Toolbar / MaterialToolbar) ===
    public final int TOP_APP_BAR_BACKGROUND_COLOR = BACKGROUND;
    public final int TOP_APP_BAR_TITLE_COLOR = ACCENT;
    public final int TOP_APP_BAR_SUBTITLE_COLOR = TEXT_LIGHT_COLOR;
    public final float TOP_APP_BAR_ELEVATION = 6f;
    // === MaterialButton ===
    public final int MATERIAL_BUTTON_TEXT_COLOR = TEXT_MID_COLOR;
    public final float MATERIAL_BUTTON_TEXT_SIZE = 16f;
    public final int MATERIAL_BUTTON_BACKGROUND_COLOR = BASE;
    public final int MATERIAL_BUTTON_STROKE_COLOR = FAINT_FOCUS_COLOR;
    public final int MATERIAL_BUTTON_STROKE_WIDTH = 2;
    public final int MATERIAL_BUTTON_CORNER_RADIUS = 12;
    public final int MATERIAL_BUTTON_RIPPLE_COLOR = BUTTON_HOVER;
    public final float MATERIAL_BUTTON_SHADOW_RADIUS = 7f;
    public final float MATERIAL_BUTTON_SHADOW_DX = 0f;
    public final float MATERIAL_BUTTON_SHADOW_DY = 0f;
    public final int MATERIAL_BUTTON_SHADOW_COLOR = CONTROL_SHADOW;
    // === LinearLayout ===
    public final int LINEAR_LAYOUT_BACKGROUND_COLOR = CONTROL_INNER_BACKGROUND;
    public final float LINEAR_LAYOUT_ELEVATION = 4f;
    // === FrameLayout ===
    public final int FRAME_LAYOUT_BACKGROUND_COLOR = CONTROL_INNER_BACKGROUND_ALT;
    public final float FRAME_LAYOUT_ELEVATION = 6f;
    // === RelativeLayout ===
    public final int RELATIVE_LAYOUT_BACKGROUND_COLOR = BASE;
    public final float RELATIVE_LAYOUT_ELEVATION = 5f;
    // === ConstraintLayout ===
    public final int CONSTRAINT_LAYOUT_BACKGROUND_COLOR = BACKGROUND;
    public final float CONSTRAINT_LAYOUT_ELEVATION = 8f;
    // === ScrollView ===
    public final int SCROLLVIEW_BACKGROUND_COLOR = CONTROL_INNER_BACKGROUND;
    public final float SCROLLVIEW_ELEVATION = 4f;
    // === HorizontalScrollView ===
    public final int HORIZONTAL_SCROLLVIEW_BACKGROUND_COLOR = CONTROL_INNER_BACKGROUND_ALT;
    public final float HORIZONTAL_SCROLLVIEW_ELEVATION = 5f;
    // === CardView ===
    public final int CARDVIEW_BACKGROUND_COLOR = CONTROL_INNER_BACKGROUND;
    public final float CARDVIEW_RADIUS = 12f;
    public final float CARDVIEW_ELEVATION = 6f;
    // === CheckBox ===
    public final int CHECKBOX_TEXT_COLOR = TEXT_MID_COLOR;
    public final float CHECKBOX_TEXT_SIZE = 14f;
    // === Chip ===
    public final int CHIP_BACKGROUND_COLOR = CONTROL_INNER_BACKGROUND;
    public final int CHIP_TEXT_COLOR = TEXT_MID_COLOR;
    public final float CHIP_CORNER_RADIUS = 8f;
    // === ProgressBar ===
    public final int PROGRESSBAR_PROGRESS_COLOR = ACCENT;
    public final int PROGRESSBAR_INDETERMINATE_COLOR = TEXT_LIGHT_COLOR;
    // === RadioButton ===
    public final int RADIOBUTTON_TINT_COLOR = ACCENT;
    public final int RADIOBUTTON_TEXT_COLOR = TEXT_MID_COLOR;
    public final float RADIOBUTTON_TEXT_SIZE = 14f;
    // === SeekBar ===
    public final int SEEKBAR_PROGRESS_COLOR = ACCENT;
    public final int SEEKBAR_THUMB_COLOR = TEXT_MID_COLOR;
    // === Switch ===
    public final int SWITCH_THUMB_COLOR = ACCENT;
    public final int SWITCH_TRACK_COLOR = BASE;
    public final int SWITCH_TEXT_COLOR = TEXT_MID_COLOR;
    public final float SWITCH_TEXT_SIZE = 14f;
    // === EditText ===
    public final int EDITTEXT_TEXT_COLOR = TEXT_MID_COLOR;
    public final int EDITTEXT_HINT_COLOR = TEXT_LIGHT_COLOR;
    public final int EDITTEXT_BACKGROUND_COLOR = CONTROL_INNER_BACKGROUND_ALT;
    public final int EDITTEXT_HIGHLIGHT_COLOR = FAINT_FOCUS_COLOR;
    public final float EDITTEXT_TEXT_SIZE = 16f;
    public final float EDITTEXT_SHADOW_RADIUS = 1f;
    public final float EDITTEXT_SHADOW_DX = 0f;
    public final float EDITTEXT_SHADOW_DY = 0f;
    public final int EDITTEXT_SHADOW_COLOR = CONTROL_SHADOW;
    // === TextView ===
    public final int TEXTVIEW_TEXT_COLOR = TEXT_MID_COLOR;
    public final float TEXTVIEW_TEXT_SIZE = 16f;
    public final int TEXTVIEW_BACKGROUND_COLOR = CONTROL_INNER_BACKGROUND;
    public final int TEXTVIEW_HIGHLIGHT_COLOR = ACCENT;
    public final float TEXTVIEW_SHADOW_RADIUS = 2f;
    public final float TEXTVIEW_SHADOW_DX = 0f;
    public final float TEXTVIEW_SHADOW_DY = 1f;
    public final int TEXTVIEW_SHADOW_COLOR = CONTROL_SHADOW;

    public MStyleRoot() {
        // Utility class — keine Instanz erlaubt
    }
}

