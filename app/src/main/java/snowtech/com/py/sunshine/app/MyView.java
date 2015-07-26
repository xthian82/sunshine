package snowtech.com.py.sunshine.app;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

/**
 * Created by cristhian on 26/7/15.
 */
public class MyView extends View {
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int DefaultStyle) {
        super(context, attrs, DefaultStyle);
    }

    @Override
    public void onMeasure(int wMeasureSpec, int hMeasureSpec) {
        int hSpecMode = MeasureSpec.getMode(hMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(hMeasureSpec);

        int wSpecMode = MeasureSpec.getMode(wMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(wMeasureSpec);


        setMeasuredDimension(wSpecSize, hSpecSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent ev) {
        ev.getText().add(windSpeedDir);
    }
}
