package com.caveflo.fragment.bloodalchohol;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.util.Log;
import android.util.SparseIntArray;

import com.caveflo.dao.Drink;

public class BloodAlcoholContentCalculator {

	public static final float womenCoef = 0.6f;
	public static final float manCoef = 0.7f;
	public static final float decreaseCoef = 0.15f;

	private float sexCoeff, max;
	private int weight;
	private int startHour;
	private List<Drink> drinks;
	private float currentBAC;
	private boolean relevant = false;

	private List<Float> values = new ArrayList<Float>();
	private List<Float> axisY = new ArrayList<Float>();
	private List<String> axisX = new ArrayList<String>();

	public BloodAlcoholContentCalculator(float sexCoeff, int weight, List<Drink> drinks, int startHour) {
		super();
		this.sexCoeff = sexCoeff;
		this.weight = weight;
		this.drinks = drinks;
		this.startHour = startHour;
	}

	public void compute() {
		values.clear();
		axisY.clear();
		axisX.clear();
		int currentHour = startHour;
		float current = 0f;
		max = 0f;
		SparseIntArray perHours = getBACPerHour();
		for (int i = 0; i < 23; i++) {
			if (current > 0) {
				relevant = true;
				if (current - decreaseCoef < 0) {
					current = 0f;
				} else {
					current -= decreaseCoef;
				}
			}
			current += (perHours.get(currentHour) / 100f);
			Log.d("Blood Compute", "At " + currentHour + " = > " + current);
			values.add(Math.round(current * 100f) / 100f);
			axisX.add(currentHour + "h");
			if (currentHour - 1 == Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
				currentBAC = Math.round(current * 100f) / 100f;
			}
			if (current > max) {
				max = current;
			}
			currentHour = (currentHour + 1) % 24;
		}

		// Removing unless data from the end
		for (int i = axisX.size() - 1; i > 1 && axisX.size() > 2; i--) {
			if (values.get(i - 1) == 0) {
				axisX.remove(i);
			} else {
				break;
			}
		}

		// Generate Y axis
		for (float j = 0f; j <= max + 0.60f; j += 0.50f) {
			axisY.add(j);
		}
	}

	public Float getResult() {
		return currentBAC;
	}

	private Float getBAC(Drink drink) {
		return ((drink.getDegree() / 100f) * (drink.getQuantity() * 10f) * 0.8f) / (sexCoeff * weight);
	}

	private SparseIntArray getBACPerHour() {
		SparseIntArray result = new SparseIntArray();
		for (Drink drink : drinks) {
			int hour = (drink.getHour() + 1) % 24;
			int value = (int) ((getBAC(drink) * 100) + result.get(hour));
			result.put(hour, value);
		}
		return result;
	}

	public List<Float> getValues() {
		return values;
	}

	public List<Float> getAxisY() {
		return axisY;
	}

	public List<String> getAxisX() {
		return axisX;
	}

	public float getMax() {
		return max;
	}

	public boolean isRelevant() {
		return relevant;
	}

}
