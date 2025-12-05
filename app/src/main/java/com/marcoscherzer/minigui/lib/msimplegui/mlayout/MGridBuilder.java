package com.marcoscherzer.minigui.lib.msimplegui.mlayout;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;

import java.util.ArrayList;
import java.util.List;

/**
 * MGridBuilder&MGridBuilder-API, Copyright Marco Scherzer Closed Source Development, All Rights Reserved
 * MGridBuilder-JavaFX Version, Copyright Marco Scherzer Closed Source Development, All Rights Reserved
 *
 * @version 0.0.1 preAlpha MGridBuilder-Android UI Version(Port-Try via Microsoft Copilot), Copyright Marco Scherzer Closed Source Development, All Rights Reserved
 * untested
 */
public class MGridBuilder {
    private final Context context;
    private final MGrid layout;
    private final List<Integer> vGuides = new ArrayList<>();
    private final List<Integer> hGuides = new ArrayList<>();
    private final List<Float> rowHeights = new ArrayList<>();
    ArrayList<ArrayList<FrameLayout>> coord2wrapperMap = new ArrayList<ArrayList<FrameLayout>>(5);//y,x
    private float[] colPercents = new float[0];
    private float cumulativeRowHeight = 0f;

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MGridBuilder(Context context) {
        this.context = context;
        layout = new MGrid(context);//new ConstraintLayout(context); //evtl später direkt MGrid
        layout.setId(View.generateViewId());
        layout.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        ));
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MGridBuilder setColumnWidths(float... percents) {
        colPercents = percents.clone();
        float cumulativePercents = 0f;
        for (int i = 0; i < percents.length - 1; i++) {
            cumulativePercents += percents[i];
            int id = View.generateViewId();
            vGuides.add(id);

            Guideline guide = new Guideline(context);
            guide.setId(id);
            ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
            );
            lp.orientation = ConstraintLayout.LayoutParams.VERTICAL;
            lp.guidePercent = cumulativePercents;
            guide.setLayoutParams(lp);
            layout.addView(guide);
        }
        return this;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MGridLine addLine(float percentHeight) {
        rowHeights.add(percentHeight);
        cumulativeRowHeight += percentHeight;

        int id = View.generateViewId();
        hGuides.add(id);

        Guideline guide = new Guideline(context);
        guide.setId(id);
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        lp.orientation = ConstraintLayout.LayoutParams.HORIZONTAL;
        lp.guidePercent = cumulativeRowHeight;
        guide.setLayoutParams(lp);
        layout.addView(guide);

        return new MGridLine(hGuides.size() - 1);
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MGrid create() {
        int rowCount = coord2wrapperMap.size();
        int colCount = coord2wrapperMap.get(0).size();

        FrameLayout[][] matrix = new FrameLayout[rowCount][colCount];
        for (int y = 0; y < rowCount; y++) {
            for (int x = 0; x < colCount; x++) {
                matrix[y][x] = coord2wrapperMap.get(y).get(x);
            }
        }
        layout.setMatrix(matrix);
        return layout;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public class MGridLine {
        private final int rowIndex;
        private final ArrayList<FrameLayout> lineOfWrappers;
        private int currentCol = 0;

        /**
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        MGridLine(int rowIndex) {
            this.rowIndex = rowIndex;
            lineOfWrappers = new ArrayList<FrameLayout>();
            coord2wrapperMap.add(lineOfWrappers);
        }


        /**
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        private MGridLine attach(
                final View view,
                final int tp, final int lp, final int bp, final int rp,
                final int tm, final int lm, final int bm, final int rm,
                final MBorderDrawableBuilder style,
                final int gravity
        ) {
            if (currentCol >= colPercents.length) {
                throw new IllegalStateException("Error: Zeile enthält zu wenige Spalten, currentCol=" + currentCol + " >= colPercents.length=" + colPercents.length);
            }

            final FrameLayout wrapper = new FrameLayout(context);
            wrapper.setId(View.generateViewId());
            MBorderDrawableBuilder.styleViewWithBackground(wrapper, style, tp, lp, bp, rp);

            final FrameLayout.LayoutParams innerLp = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
            );
            innerLp.gravity = gravity;
            view.setLayoutParams(innerLp);
            applyGravity(view, gravity);
            wrapper.addView(view);
//----------------------------------------------------------------------------
            final int topId = rowIndex == 0 ? layout.getId() : hGuides.get(rowIndex - 1);
            final int bottomId = hGuides.get(rowIndex);
            final int leftId = currentCol == 0 ? layout.getId() : vGuides.get(currentCol - 1);
            final int rightId = currentCol == colPercents.length - 1 ? layout.getId() : vGuides.get(currentCol);

            final ConstraintLayout.LayoutParams cellLp = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                    ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
            );

            cellLp.topToTop = topId;
            cellLp.bottomToBottom = bottomId;
            cellLp.startToStart = leftId;
            cellLp.endToEnd = rightId;
            cellLp.setMargins(lm, tm, rm, bm);

            layout.addView(wrapper, cellLp);
//-------------------------------------------------------------------------------
            // Zelle in 2D-Map registrieren
            lineOfWrappers.add(wrapper);

            currentCol++;
            return this;
        }

        /**
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        // 1) default, no background, default gravity CENTER
        public MGridLine add(View view) {
            return attach(
                    view,
                    /*tp*/0, /*lp*/0, /*bp*/0, /*rp*/0,
                    /*tm*/0, /*lm*/0, /*bm*/0, /*rm*/0,
                    /*style*/null,
                    /*gravity*/Gravity.CENTER
            );
        }

        /**
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        // 2) default + MCStyle, default gravity CENTER
        public MGridLine add(View view, MBorderDrawableBuilder style) {
            return attach(
                    view,
                    0, 0, 0, 0,
                    0, 0, 0, 0,
                    style,
                    Gravity.CENTER
            );
        }

        /**
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        // 3) uniform Margin, no background, default gravity CENTER
        public MGridLine addWithMargins(View view, int allMargin) {
            return attach(
                    view,
                    0, 0, 0, 0,
                    allMargin, allMargin, allMargin, allMargin,
                    null,
                    Gravity.CENTER
            );
        }

        /**
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        // 3) Margins, no background, default gravity CENTER
        public MGridLine addWithMargins(View view, int tm, int lm, int bm, int rm) {
            return attach(
                    view,
                    0, 0, 0, 0,
                    tm, lm, bm, rm,
                    null,
                    Gravity.CENTER
            );
        }

        /**
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        // 4) uniform Margin + MCStyle, default gravity CENTER
        public MGridLine addWithMargins(View view, int allMargin, MBorderDrawableBuilder style) {
            return attach(
                    view,
                    0, 0, 0, 0,
                    allMargin, allMargin, allMargin, allMargin,
                    style,
                    Gravity.CENTER
            );
        }

        /**
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        // 5) individual Insets (Padding) + MCStyle, default gravity CENTER
        public MGridLine addWithPadding(
                View view,
                int tp, int lp, int bp, int rp,
                MBorderDrawableBuilder style
        ) {
            return attach(
                    view,
                    tp, lp, bp, rp,
                    0, 0, 0, 0,
                    style,
                    Gravity.CENTER
            );
        }

        /**
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        // 6) uniform Padding, no background, default gravity CENTER
        public MGridLine addWithPaddings(View view, int allPadding) {
            return attach(
                    view,
                    allPadding, allPadding, allPadding, allPadding,
                    0, 0, 0, 0,
                    null,
                    Gravity.CENTER
            );
        }

        /**
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        // 7) uniform Padding, no background, default gravity CENTER
        public MGridLine addWithPaddings(View view, int tp, int lp, int bp, int rp) {
            return attach(
                    view, tp, lp, bp, rp,
                    0, 0, 0, 0,
                    null,
                    Gravity.CENTER
            );
        }

        /**
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        // 7) uniform Padding + MCStyle, default gravity CENTER
        public MGridLine addWithPaddings(View view, int allPadding, MBorderDrawableBuilder style) {
            return attach(
                    view,
                    allPadding, allPadding, allPadding, allPadding,
                    0, 0, 0, 0,
                    style,
                    Gravity.CENTER
            );
        }

        /**
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        // 8) uniform Padding + uniform Margin, no background, default gravity CENTER
        public MGridLine addWithMarginsAndPaddings(View view,
                                                   int allPadding,
                                                   int allMargin
        ) {
            return attach(
                    view,
                    allPadding, allPadding, allPadding, allPadding,
                    allMargin, allMargin, allMargin, allMargin,
                    null,
                    Gravity.CENTER
            );
        }

        /**
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        // 9) uniform Padding + uniform Margin + MCStyle, default gravity CENTER
        public MGridLine addWithMarginsAndPaddings(View view,
                                                   int allPadding,
                                                   int allMargin,
                                                   MBorderDrawableBuilder style
        ) {
            return attach(
                    view,
                    allPadding, allPadding, allPadding, allPadding,
                    allMargin, allMargin, allMargin, allMargin,
                    style,
                    Gravity.CENTER
            );
        }

        /**
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        // 10) individuelle Insets + individuelle Margins + MCStyle, default gravity CENTER
        public MGridLine addWithMarginAndPadding(
                View view,
                int tp, int lp, int bp, int rp,
                int tm, int lm, int bm, int rm,
                MBorderDrawableBuilder style
        ) {
            return attach(
                    view,
                    tp, lp, bp, rp,
                    tm, lm, bm, rm,
                    style,
                    Gravity.CENTER
            );
        }

        //----------------------------------------------------------------------------------------------------------------------------

        /**
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         * unready and untested
         */
        private void applyGravity(View view, int gravity) {
            if (view instanceof RadioGroup) {
                ((RadioGroup) view).setGravity(gravity);


            } else if (view instanceof TextView) {
                ((TextView) view).setGravity(gravity);

            } else if (view instanceof LinearLayout) {
                ((LinearLayout) view).setGravity(gravity);

            } else if (view instanceof FrameLayout) {
                view.setForegroundGravity(gravity);
            }
        }

        /***
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
        unready and untested
         * */
        // 1) View + Gravity
        public MGridLine add(View view, int gravity) {
            return attach(
                    view,
                    /*tp*/0, /*lp*/0, /*bp*/0, /*rp*/0,
                    /*tm*/0, /*lm*/0, /*bm*/0, /*rm*/0,
                    /*style*/null,
                    gravity
            );
        }

        /***
         @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
          * unready and untested
          * */
        // 2) View + MCStyle + Gravity
        public MGridLine add(View view, MBorderDrawableBuilder style, int gravity) {
            return attach(
                    view,
                    0, 0, 0, 0,
                    0, 0, 0, 0,
                    style,
                    gravity
            );
        }

        /***
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         * unready and untested
         * */
        // 3) uniform Margin + Gravity
        public MGridLine addWithMargins(View view, int allMargin, int gravity) {
            return attach(
                    view,
                    0, 0, 0, 0,
                    allMargin, allMargin, allMargin, allMargin,
                    null,
                    gravity
            );
        }

        /***
         @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
          * unready and untested
          * */
        // 4) uniform Margin + MCStyle + Gravity
        public MGridLine addWithMargins(View view, int allMargin, MBorderDrawableBuilder style, int gravity) {
            return attach(
                    view,
                    0, 0, 0, 0,
                    allMargin, allMargin, allMargin, allMargin,
                    style,
                    gravity
            );
        }

        /***
         @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
          * unready and untested
          * */
        // 5) individual Insets + MCStyle + Gravity
        public MGridLine addWithPadding(
                View view,
                int tp, int lp, int bp, int rp,
                MBorderDrawableBuilder style,
                int gravity
        ) {
            return attach(
                    view,
                    tp, lp, bp, rp,
                    0, 0, 0, 0,
                    style,
                    gravity
            );
        }

        /***
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         * unready and untested
         * */
        // 6) uniform Padding + Gravity
        public MGridLine addWithPaddings(View view, int allPadding, int gravity) {
            return attach(
                    view,
                    allPadding, allPadding, allPadding, allPadding,
                    0, 0, 0, 0,
                    null,
                    gravity
            );
        }

        /***
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         * unready and untested
         * */
        // 7) uniform Padding + MCStyle + Gravity
        public MGridLine addWithPaddings(View view, int allPadding, MBorderDrawableBuilder style, int gravity) {
            return attach(
                    view,
                    allPadding, allPadding, allPadding, allPadding,
                    0, 0, 0, 0,
                    style,
                    gravity
            );
        }

        /*** @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         * unready and untested
         * */
        // 8) uniform Padding + uniform Margin + Gravity
        public MGridLine addWithMarginsAndPaddings(View view, int allPadding, int allMargin, int gravity) {
            return attach(
                    view,
                    allPadding, allPadding, allPadding, allPadding,
                    allMargin, allMargin, allMargin, allMargin,
                    null,
                    gravity
            );
        }

        /**
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         * unready and untested
         */
        // 9) uniform Padding + uniform Margin + MCStyle + Gravity
        public MGridLine addWithMarginsAndPaddings(View view, int allPadding, int allMargin, MBorderDrawableBuilder style, int gravity) {
            return attach(
                    view,
                    allPadding, allPadding, allPadding, allPadding,
                    allMargin, allMargin, allMargin, allMargin,
                    style,
                    gravity
            );
        }

        /**
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         * unready and untested
         */
        // 10) individual Insets + individual Margins + MCStyle + Gravity
        public MGridLine addWithMarginAndPadding(
                View view,
                int tp, int lp, int bp, int rp,
                int tm, int lm, int bm, int rm,
                MBorderDrawableBuilder style,
                int gravity
        ) {
            return attach(
                    view,
                    tp, lp, bp, rp,
                    tm, lm, bm, rm,
                    style,
                    gravity
            );
        }


    }

}


















