/*
 *  Copyright(c) 2017 lizhaotailang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.marktony.espresso.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Dimension;
import android.util.AttributeSet;
import android.view.View;

import io.github.marktony.espresso.R;

/**
 * Created by lizhaotailang on 2017/3/4.
 */

public class Timeline extends View {

    @Dimension private int atomSize = 24;
    @Dimension private int lineSize = 12;

    private Drawable startLine;
    private Drawable finishLine;
    private Drawable atomDrawable;

    public Timeline(Context context) {
        this(context, null);
    }

    public Timeline(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Timeline(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.Timeline);

        atomSize = typedArray.getDimensionPixelSize(R.styleable.Timeline_atom_size, atomSize);
        lineSize = typedArray.getDimensionPixelSize(R.styleable.Timeline_line_size, lineSize);

        startLine = typedArray.getDrawable(R.styleable.Timeline_start_line);
        finishLine = typedArray.getDrawable(R.styleable.Timeline_finish_line);
        atomDrawable = typedArray.getDrawable(R.styleable.Timeline_atom);

        typedArray.recycle();

        if (startLine != null) {
            startLine.setCallback(this);
        }

        if (finishLine != null) {
            finishLine.setCallback(this);
        }

        if (atomDrawable != null) {
            atomDrawable.setCallback(this);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (startLine != null) {
            startLine.draw(canvas);
        }

        if (finishLine != null) {
            finishLine.draw(canvas);
        }

        if (atomDrawable != null) {
            atomDrawable.draw(canvas);
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = getPaddingLeft() + getPaddingRight();
        int h = getPaddingTop() + getPaddingBottom();

        if (atomDrawable != null) {
            w += atomSize;
            h += atomSize;
        }

        w = Math.max(w, getMeasuredWidth());
        h = Math.max(h, getMeasuredHeight());

        int width = resolveSizeAndState(w, widthMeasureSpec, 0);
        int height = resolveSizeAndState(h, heightMeasureSpec, 0);

        setMeasuredDimension(width, height);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initDrawableSize();
    }

    private void initDrawableSize() {

        int pLeft = getPaddingLeft();
        int pRight = getPaddingRight();
        int pTop = getPaddingTop();
        int pBottom = getPaddingBottom();

        int width = getWidth();
        int height = getHeight();

        int cWidth = width - pLeft - pRight;
        int cHeight = height - pTop - pBottom;

        Rect bounds;

        if (atomDrawable != null) {

            int atomSize = Math.min(this.atomSize, Math.min(cWidth, cHeight));
            atomDrawable.setBounds(pLeft, pTop, pLeft + atomSize, pTop + atomSize);

            bounds = atomDrawable.getBounds();
        } else {
            bounds = new Rect(pLeft, pTop, pLeft + cWidth, pTop + cHeight);
        }

        int halfLineSize = lineSize >> 1;
        int lineLeft = bounds.centerX() - halfLineSize;

        if (startLine != null) {
            startLine.setBounds(lineLeft, 0, lineLeft + lineSize, bounds.top);
        }

        if (finishLine != null) {
            finishLine.setBounds(lineLeft, bounds.bottom, lineLeft + lineSize, height);
        }

    }

    public void setLineSize(int lineSize) {
        if (this.lineSize != lineSize) {
            this.lineSize = lineSize;
            initDrawableSize();
            invalidate();
        }
    }

    public void setAtomSize(int atomSize) {
        if (this.atomSize != atomSize) {
            this.atomSize = atomSize;
            initDrawableSize();
            invalidate();
        }
    }

    public void setStartLine(Drawable startLine) {
        if (this.startLine != startLine) {
            this.startLine = startLine;
            if (this.startLine != null) {
                this.startLine.setCallback(this);
            }
            initDrawableSize();
            invalidate();
        }
    }

    public void setFinishLine(Drawable finishLine) {
        if (this.finishLine != finishLine) {
            this.finishLine = finishLine;
            if (this.finishLine != null) {
                this.finishLine.setCallback(this);
            }
            initDrawableSize();
            invalidate();
        }
    }

    public void setAtomDrawable(Drawable atomDrawable) {
        if (this.atomDrawable != atomDrawable) {
            this.atomDrawable = atomDrawable;
            if (this.atomDrawable != null) {
                this.atomDrawable.setCallback(this);
            }
            initDrawableSize();
            invalidate();
        }
    }

}
