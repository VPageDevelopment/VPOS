package com.vpage.vpos.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.vpage.vpos.R;
import java.util.Random;

public class StoreConfigGridImageAdapter extends BaseAdapter {

	private static final String TAG = StoreConfigGridImageAdapter.class.getName();

	private Animator mCurrentAnimator;

	private int mShortAnimationDuration = 2;

	private Activity activity;

	static Random random = new Random();
	String[] moduleNameArray;

	public StoreConfigGridImageAdapter(Activity activity) {
		this.activity = activity;

		moduleNameArray = activity.getResources().getStringArray(R.array.storeConfigModuleName);
	}

	public int getCount() {
		return moduleNameArray.length;
	}

	public Object getItem(int position) {
		return moduleNameArray[position];
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		final ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(activity);
		} else {
			imageView = (ImageView) convertView;
		}

		imageView.setImageBitmap(getThumb(moduleNameArray[position]));
    //    int imageTag = activity.getResources().getIdentifier(moduleNameArray[position], "drawable", activity.getPackageName());
	//	Log.d(TAG,"imageTag: "+ imageTag);
	//	imageView.setImageResource( imageTag );
		//imageView.setTag(getThumb(moduleNameArray[position]));
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//int id = (Integer) arg0.getTag();
			//	Log.d(TAG,"TagValue: "+ id);

				Bitmap bitmapImage = getThumb(moduleNameArray[position]);
				int pixel = bitmapImage.getPixel(5,5);
				zoomImageFromThumb(arg0,getClickedImage(moduleNameArray[position],pixel));
			}
		});

		return imageView;
	}

	private void zoomImageFromThumb(final View thumbView, Bitmap bitmap) {
		// If there's an animation in progress, cancel it immediately and
		// proceed with this one.
		if (mCurrentAnimator != null) {
			mCurrentAnimator.cancel();
		}

		// Load the high-resolution "zoomed-in" image.
		final ImageView expandedImageView = (ImageView)activity
				.findViewById(R.id.expanded_image);
		//expandedImageView.setImageResource(imageResId);


		expandedImageView.setImageBitmap(bitmap);

		// Calculate the starting and ending bounds for the zoomed-in image.
		// This step
		// involves lots of math. Yay, math.
		final Rect startBounds = new Rect();
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();

		// The start bounds are the global visible rectangle of the thumbnail,
		// and the
		// final bounds are the global visible rectangle of the container view.
		// Also
		// set the container view's offset as the origin for the bounds, since
		// that's
		// the origin for the positioning animation properties (X, Y).
		thumbView.getGlobalVisibleRect(startBounds);
		activity.findViewById(R.id.container)
				.getGlobalVisibleRect(finalBounds, globalOffset);
		startBounds.offset(-globalOffset.x, -globalOffset.y);
		finalBounds.offset(-globalOffset.x, -globalOffset.y);

		// Adjust the start bounds to be the same aspect ratio as the final
		// bounds using the
		// "center crop" technique. This prevents undesirable stretching during
		// the animation.
		// Also calculate the start scaling factor (the end scaling factor is
		// always 1.0).
		float startScale;
		if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds
				.width() / startBounds.height()) {
			// Extend start bounds horizontally
			startScale = (float) startBounds.height() / finalBounds.height();
			float startWidth = startScale * finalBounds.width();
			float deltaWidth = (startWidth - startBounds.width()) / 2;
			startBounds.left -= deltaWidth;
			startBounds.right += deltaWidth;
		} else {
			// Extend start bounds vertically
			startScale = (float) startBounds.width() / finalBounds.width();
			float startHeight = startScale * finalBounds.height();
			float deltaHeight = (startHeight - startBounds.height()) / 2;
			startBounds.top -= deltaHeight;
			startBounds.bottom += deltaHeight;
		}

		// Hide the thumbnail and show the zoomed-in view. When the animation
		// begins,
		// it will position the zoomed-in view in the place of the thumbnail.
		thumbView.setAlpha(0f);
		expandedImageView.setVisibility(View.VISIBLE);

		// Set the pivot point for SCALE_X and SCALE_Y transformations to the
		// top-left corner of
		// the zoomed-in view (the default is the center of the view).
		expandedImageView.setPivotX(0f);
		expandedImageView.setPivotY(0f);

		// Construct and run the parallel animation of the four translation and
		// scale properties
		// (X, Y, SCALE_X, and SCALE_Y).
		AnimatorSet set = new AnimatorSet();
		set.play(
				ObjectAnimator.ofFloat(expandedImageView, View.X,
						startBounds.left, finalBounds.left))
				.with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
						startBounds.top, finalBounds.top))
				.with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
						startScale, 1f))
				.with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y,
						startScale, 1f));
		set.setDuration(mShortAnimationDuration);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mCurrentAnimator = null;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				mCurrentAnimator = null;
			}
		});
		set.start();
		mCurrentAnimator = set;

		// Upon clicking the zoomed-in image, it should zoom back down to the
		// original bounds
		// and show the thumbnail instead of the expanded image.
		final float startScaleFinal = startScale;
		expandedImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mCurrentAnimator != null) {
					mCurrentAnimator.cancel();
				}

				// Animate the four positioning/sizing properties in parallel,
				// back to their
				// original values.
				AnimatorSet set = new AnimatorSet();
				set.play(
						ObjectAnimator.ofFloat(expandedImageView, View.X,
								startBounds.left))
						.with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
								startBounds.top))
						.with(ObjectAnimator.ofFloat(expandedImageView,
								View.SCALE_X, startScaleFinal))
						.with(ObjectAnimator.ofFloat(expandedImageView,
								View.SCALE_Y, startScaleFinal));
				set.setDuration(mShortAnimationDuration);
				set.setInterpolator(new DecelerateInterpolator());
				set.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						thumbView.setAlpha(1f);
						expandedImageView.setVisibility(View.GONE);
						mCurrentAnimator = null;
					}

					@Override
					public void onAnimationCancel(Animator animation) {
						thumbView.setAlpha(1f);
						expandedImageView.setVisibility(View.GONE);
						mCurrentAnimator = null;
					}
				});
				set.start();
				mCurrentAnimator = set;
			}
		});
	}


	private Bitmap getThumb(String imageString)
	{
		Bitmap bmp = Bitmap.createBitmap(150, 150, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bmp);
		Paint paint = new Paint();

		paint.setColor(Color.rgb(random.nextInt(128), random.nextInt(128), random.nextInt(128)));
		paint.setTextSize(24);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		canvas.drawRect(new Rect(0, 0, 150, 150), paint);
		paint.setColor(Color.WHITE);
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(imageString, 75, 75, paint);

		return bmp;
	}


	private Bitmap getClickedImage(String imageString,int pixel)
	{
		Bitmap bmp = Bitmap.createBitmap(150, 150, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bmp);
		Paint paint = new Paint();
		//paint.setColor(Color.rgb(random.nextInt(128), random.nextInt(128), random.nextInt(128)));
		paint.setColor(pixel);
		paint.setTextSize(24);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		canvas.drawRect(new Rect(0, 0, 300, 300), paint);
		paint.setColor(Color.WHITE);
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(imageString, 75, 75, paint);

		return bmp;
	}


}