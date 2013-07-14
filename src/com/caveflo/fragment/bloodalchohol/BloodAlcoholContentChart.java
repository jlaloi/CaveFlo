package com.caveflo.fragment.bloodalchohol;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.caveflo.R;

public class BloodAlcoholContentChart extends View {

	private List<String> headerX;
	private List<Float> headerY;
	private List<Float> values;
	private Paint paint = new Paint();
	private float width, height, paddingX, paddingY, scale;
	private float oldX, oldY, newX, newY;
	private float strokeSize = 4f;
	boolean defined = false;
	private Context context;

	public BloodAlcoholContentChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public BloodAlcoholContentChart(Context context) {
		super(context);
		this.context = context;
	}

	public void setContent(List<String> headerX, List<Float> headerY, List<Float> values) {
		this.headerX = headerX;
		this.headerY = headerY;
		this.values = values;
		defined = true;
		invalidate();
	}
	
	public void hideContent(){
		defined = false;
		invalidate();
	}

	protected void onDraw(Canvas canvas) {
		width = getWidth();
		height = getHeight();
		if (defined) {
			paint.setStrokeWidth(strokeSize);
			paddingX = width / (headerX.size() + 1);
			paddingY = height / (headerY.size() + 1);
			scale = (height - paddingY) / (headerY.get(headerY.size() - 1));

			// Limit
			paint.setColor(context.getResources().getColor(R.color.chartlimit));
			canvas.drawLine(paddingX, height - paddingY - (BloodAlcoholContent.limit * scale) - strokeSize / 2, width, height - paddingY - (BloodAlcoholContent.limit * scale) - strokeSize / 2, paint);

			// Values
			oldX = paddingX;
			oldY = height - paddingY - (values.get(0) * scale + strokeSize / 2);
			paint.setColor(context.getResources().getColor(R.color.chartline));
			for (int i = 1; i < headerX.size(); i++) {
				newY = height - paddingY - (values.get(i) * scale + strokeSize / 2);
				newX = oldX + paddingX;
				canvas.drawLine(oldX, oldY, newX, newY, paint);
				oldX = newX;
				oldY = newY;
			}
			newY = height - paddingY - (values.get(headerX.size()) * scale + strokeSize / 2);
			newX = oldX + paddingX;
			canvas.drawLine(oldX, oldY, newX, newY, paint);

			// Axis
			paint.setColor(context.getResources().getColor(R.color.chartaxis));
			canvas.drawLine(paddingX, 0f, paddingX, height - paddingY, paint);
			canvas.drawLine(paddingX - strokeSize / 2, height - paddingY, width, height - paddingY, paint);
			for (int y = 0; y < headerY.size(); y++) {
				canvas.drawText(headerY.get(y) + "", 0, height - paddingY - (headerY.get(y) * scale), paint);
			}
			for (int x = 0; x < headerX.size(); x++) {
				canvas.drawText(headerX.get(x) + "", (x + 1) * paddingX, height, paint);
			}
		}
	}

}
