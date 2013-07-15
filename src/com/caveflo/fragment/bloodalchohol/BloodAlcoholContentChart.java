package com.caveflo.fragment.bloodalchohol;

import java.util.ArrayList;
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
	private float paddingXScale;
	private float oldX, oldY, newX, newY;
	private float strokeSize = 4f;
	private float segmentSize = 4f;
	private int colorAxis;
	private int colorValue;
	boolean defined = false;
	private Context context;
	private List<Float[]> pointToDisplay;

	public BloodAlcoholContentChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		configure();
	}

	public BloodAlcoholContentChart(Context context) {
		super(context);
		this.context = context;
		configure();
	}

	private void configure() {
		pointToDisplay = new ArrayList<Float[]>();
		paint.setTextSize(paint.getTextSize() * 1.5f);
		paint.setStrokeWidth(strokeSize);
		paddingY = paint.getTextSize();
		colorValue = context.getResources().getColor(R.color.chartline);
		colorAxis = context.getResources().getColor(R.color.chartaxis);
	}

	public void setContent(List<String> headerX, List<Float> headerY, List<Float> values) {
		this.headerX = headerX;
		this.headerY = headerY;
		this.values = values;
		defined = true;
		invalidate();
	}

	public void hideContent() {
		defined = false;
		invalidate();
	}

	protected void onDraw(Canvas canvas) {
		if (defined) {
			// Configuration
			width = getWidth();
			height = getHeight();
			pointToDisplay.clear();
			getXAxisScaleMaxSize();
			paddingX = (width - paddingXScale) / (headerX.size() - 1);
			scale = (height - paddingY) / (headerY.get(headerY.size() - 1));

			// Limit
			paint.setColor(context.getResources().getColor(R.color.chartlimit));
			canvas.drawLine(paddingXScale, height - paddingY - (BloodAlcoholContent.limit * scale), width, height - paddingY - (BloodAlcoholContent.limit * scale), paint);

			// Values
			oldX = paddingXScale;
			oldY = height - paddingY - (values.get(0) * scale + strokeSize / 2);
			paint.setColor(colorValue);
			for (int i = 1; i < headerX.size(); i++) {
				newY = height - paddingY - (values.get(i) * scale + strokeSize / 2);
				newX = oldX + paddingX;
				canvas.drawLine(oldX, oldY, newX, newY, paint);
				if (oldY > newY) {
					pointToDisplay.add(new Float[] { values.get(i), newX, newY - strokeSize });
				}
				oldX = newX;
				oldY = newY;
			}

			// Display point
			paint.setColor(colorAxis);
			for (Float[] points : pointToDisplay) {
				String toDisplay = points[0] + "";
				canvas.drawText(toDisplay, points[1] - paint.measureText(toDisplay) / 2, points[2], paint);
			}

			// Axis
			paint.setColor(colorAxis);
			paint.setStrokeWidth(strokeSize / 2);
			canvas.drawLine(paddingXScale, 0f, paddingXScale, height - paddingY, paint);
			canvas.drawLine(paddingXScale, height - paddingY, width, height - paddingY, paint);
			for (int y = 0; y < headerY.size(); y++) {
				canvas.drawLine(paddingXScale, height - paddingY - (headerY.get(y) * scale), segmentSize + paddingXScale, height - paddingY - (headerY.get(y) * scale), paint);
				canvas.drawText(headerY.get(y) + "", 0, height - paddingY - (headerY.get(y) * scale), paint);
			}
			for (int x = 0; x < headerX.size(); x++) {
				canvas.drawLine(paddingXScale + x * paddingX, height - paddingY, paddingXScale + x * paddingX, height - paddingY - segmentSize, paint);
				if (headerX.size() < 18 || x % 2 == 0) {
					canvas.drawText(headerX.get(x) + "", paddingXScale + x * paddingX, height, paint);
				}
			}
		}
	}

	private void getXAxisScaleMaxSize() {
		paddingXScale = 0f;
		float current;
		for (float value : headerY) {
			current = paint.measureText(value + " ");
			if (current > paddingXScale) {
				paddingXScale = current;
			}
		}
	}

}
