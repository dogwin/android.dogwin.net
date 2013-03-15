package wei.ye.mplayer;

import java.util.ArrayList;
import java.util.List;

import wei.ye.mplayer.YcMusicLrc.LyricContent;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
/**
 * 重写TextView
 * @author 叶超
 *
 */
public class YcMusicLrcView extends TextView {
	private float high;
	private float width;
	private Paint CurrentPaint;
	private Paint NotCurrentPaint;
	private float TextHigh = 25;
	private float TextSize = 15;
	private int Index = 0;
	private List<LyricContent> mSentenceEntities = new ArrayList<LyricContent>();

	public void setSentenceEntities(List<LyricContent> mSentenceEntities) {
		this.mSentenceEntities = mSentenceEntities;
	}

	public YcMusicLrcView(Context context) {
		super(context);
		init();
	}

	public YcMusicLrcView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public YcMusicLrcView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setFocusable(true);

		// 高亮部分
		CurrentPaint = new Paint();
		CurrentPaint.setAntiAlias(true);
		CurrentPaint.setTextAlign(Paint.Align.CENTER);

		// 非高亮部分
		NotCurrentPaint = new Paint();
		NotCurrentPaint.setAntiAlias(true);
		NotCurrentPaint.setTextAlign(Paint.Align.CENTER);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (canvas == null) {
			return;
		}

		CurrentPaint.setColor(Color.CYAN);
		NotCurrentPaint.setColor(Color.WHITE);

		CurrentPaint.setTextSize(18);
		CurrentPaint.setTypeface(Typeface.SERIF);

		NotCurrentPaint.setTextSize(TextSize);
		NotCurrentPaint.setTypeface(Typeface.SERIF);

		try {
			canvas.drawText(mSentenceEntities.get(Index).getLyric(), width / 2,
					high / 2, CurrentPaint);

			float tempY = high / 2;
			// 画出本句之前的句子
			for (int i = Index - 1; i >= 0; i--) {

				// 向上推移
				tempY = tempY - TextHigh;

				canvas.drawText(mSentenceEntities.get(i).getLyric(), width / 2,
						tempY, NotCurrentPaint);

			}

			tempY = high / 2;
			// 画出本句之后的句子
			for (int i = Index + 1; i < mSentenceEntities.size(); i++) {
				// 往下推移
				tempY = tempY + TextHigh;

				canvas.drawText(mSentenceEntities.get(i).getLyric(), width / 2,
						tempY, NotCurrentPaint);

			}
		} catch (Exception e) {
		}

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		this.high = h;
		this.width = w;
	}

	public void SetIndex(int index) {
		this.Index = index;
	}
}
