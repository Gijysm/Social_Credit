package com.example.social_credit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.social_credit.R;

import kotlin.random.Random;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView text;
    private ImageView imageBackGround;
    private int i = 0;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        imageBackGround = findViewById(R.id.imageBackGround);
        text = findViewById(R.id.textView);
        mediaPlayer = MediaPlayer.create(this, R.raw.mao_zedong);

        imageView.post(() -> {
            int width = imageView.getWidth();
            int height = imageView.getHeight();

            imageView.setOnClickListener(v -> {
                text.setText(String.valueOf(i++));

                if (imageView.getLayoutParams().width == width && imageView.getLayoutParams().height == height) {
                    imageView.getLayoutParams().width = 706;
                    imageView.getLayoutParams().height = 706;
                    fadeImage(imageView, this);
                    rotateAnim(imageView);
                } else {
                    fadeImage(imageView, this);
                    rotateAnim(imageView);
                    imageView.getLayoutParams().width = width;
                    imageView.getLayoutParams().height = height;
                }

                if (i % 2 == 0 && i % 20 == 0) {
                    imageView.setImageResource(R.drawable.velikiysoncelikiy);
                    imageBackGround.setImageResource(R.drawable.velikiysoncelikiy);
                    mediaPlayer.setPlaybackParams(new PlaybackParams().setSpeed(1f).setPitch(0.8f));
                    mediaPlayer.start();
                }
                imageView.requestLayout();
            });
        });

        mediaPlayer.setVolume(500f, 500f);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            final androidx.core.graphics.Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void fadeImage(ImageView view, Context context) {
        Animation fadeAnim = AnimationUtils.loadAnimation(context, R.anim.fade);
        view.startAnimation(fadeAnim);
    }

    private void rotateAnim(ImageView view) {
        RotateAnimation rotate = new RotateAnimation(
                Random.Default.nextFloat() * (80 - (-50)) + (-50),
                Random.Default.nextFloat() * (80 - (-50)) + (-50),
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotate.setDuration(2000);
        view.startAnimation(rotate);
    }

    private void createParticleImage(ImageView view, int intx, int inty, Context context) {
        ImageView particleImage = new ImageView(context);
        particleImage.setImageResource(R.drawable.images);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        ((ViewGroup) view.getParent()).addView(particleImage, params);

        ObjectAnimator animation = ObjectAnimator.ofFloat(
                particleImage, "translationY",
                inty, inty - 200f
        );
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ((ViewGroup) view.getParent()).removeView(particleImage);
            }
        });
        animation.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
