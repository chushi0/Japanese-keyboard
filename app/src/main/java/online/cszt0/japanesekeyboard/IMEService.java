package online.cszt0.japanesekeyboard;

import android.annotation.SuppressLint;
import android.inputmethodservice.InputMethodService;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import online.cszt0.japanesekeyboard.util.Dictionary;
import online.cszt0.japanesekeyboard.util.UiUtils;
import online.cszt0.japanesekeyboard.view.StackLayout;

public class IMEService extends InputMethodService {

	private TextView input;
	private TextView candidate;

	private StackLayout keyboardLayout;
	private StackLayout bottomLayout;

	// keyboard - main
	private Button[] alphabetKey;
	private Button capslockButton;
	private Button deleteButton;

	// keyboard - num
	private Button numBackButton;

	// bottom
	private Button numberlockButton;
	private Button spaceButton;
	private Button enterButton;
	private Button commaButton;
	private Button periodButton;

	private InputMode inputMode = InputMode.HIRAGANA;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public void onCreate() {
		super.onCreate();

		@SuppressLint("InflateParams")
		View candidates = getLayoutInflater().inflate(R.layout.ime_input, null);
		@SuppressLint("InflateParams")
		View view = getLayoutInflater().inflate(R.layout.ime_main, null);
		// findViewsById
		keyboardLayout = view.findViewById(R.id.keyboard_layout);
		bottomLayout = view.findViewById(R.id.bottom_layout);
		View.OnTouchListener keyTouchMode = new View.OnTouchListener() {
			private void run(View v) {
				if (v.getTag() != null) {
					v.performClick();
					Runnable task = () -> run(v);
					v.setTag(task);
					v.postDelayed(task, 20);
				}
			}

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					v.performClick();
					v.setPressed(true);
					Runnable task = () -> run(v);
					v.setTag(task);
					v.postDelayed(task, 500);
				} else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
					v.setPressed(false);
					v.removeCallbacks((Runnable) v.getTag());
					v.setTag(null);
				}
				return true;
			}
		};
		input = candidates.findViewById(R.id.input);
		candidate = view.findViewById(R.id.candidate);
		candidate.setOnClickListener(v -> {
			String text = candidate.getText().toString();
			if (!text.isEmpty()) {
				getCurrentInputConnection().commitText(text, 1);
				input.setText(null);
				candidate.setText(null);
				enterButton.setText("┘");
			}
		});
		alphabetKey = new Button[26];
		View.OnClickListener alphabetKeyListener = v -> {
			Button button = (Button) v;
			String text = button.getText().toString().toLowerCase();
			input.append(text);
			updateCandidate();
			enterButton.setText("×");
			setCandidatesViewShown(true);
		};
		for (int i = 0; i < 26; i++) {
			char c = (char) ('a' + i);
			String idName = "key_" + c;
			int id = UiUtils.getResourcesIdByName(idName);
			alphabetKey[i] = view.findViewById(id);
			alphabetKey[i].setOnTouchListener(keyTouchMode);
			alphabetKey[i].setOnClickListener(alphabetKeyListener);
		}
		capslockButton = view.findViewById(R.id.key_capslock);
		capslockButton.setOnClickListener(v -> {
			inputMode = inputMode.next();
			updateCandidate();
		});
		deleteButton = view.findViewById(R.id.key_del);
		deleteButton.setOnTouchListener(keyTouchMode);
		deleteButton.setOnClickListener(v -> {
			if (input.getText().length() == 0) {
				sendDownUpKeyEvents(KeyEvent.KEYCODE_DEL);
			} else {
				String text = input.getText().toString();
				text = text.substring(0, text.length() - 1);
				input.setText(text);
				if (text.isEmpty()) {
					candidate.setText(null);
					enterButton.setText("┘");
				} else {
					updateCandidate();
				}
			}
		});
		numBackButton = view.findViewById(R.id.key_num_back);
		numBackButton.setOnClickListener(v -> {
			// 转为主键盘
			bottomLayout.setVisibility(View.VISIBLE);
			keyboardLayout.pop();
		});
		numberlockButton = view.findViewById(R.id.key_numlock);
		numberlockButton.setOnClickListener(v -> {
			input.setText(null);
			candidate.setText(null);
			enterButton.setText("┘");
			// 转换为数字键盘
			bottomLayout.setVisibility(View.GONE);
			keyboardLayout.push(1);
		});
		spaceButton = view.findViewById(R.id.key_space);
		spaceButton.setOnTouchListener(keyTouchMode);
		spaceButton.setOnClickListener(v -> {
			String text = candidate.getText().toString();
			if (text.isEmpty()) {
				// 日文全角空格
				sendKeyChar('　');
			} else {
				candidate.performClick();
			}
		});
		enterButton = view.findViewById(R.id.key_enter);
		enterButton.setOnClickListener(v -> {
			if (input.getText().length() == 0) {
				sendDownUpKeyEvents(KeyEvent.KEYCODE_ENTER);
			} else {
				input.setText(null);
				candidate.setText(null);
				enterButton.setText("┘");
			}
		});
		commaButton = view.findViewById(R.id.key_comma);
		commaButton.setOnTouchListener(keyTouchMode);
		commaButton.setOnClickListener(v -> {
			if (input.getText().length() > 0) {
				candidate.performClick();
			}
			// 日文中逗号输入的不是“，”，而是“、”
			sendKeyChar('、');
		});
		periodButton = view.findViewById(R.id.key_period);
		periodButton.setOnTouchListener(keyTouchMode);
		periodButton.setOnClickListener(v -> {
			if (input.getText().length() > 0) {
				candidate.performClick();
			}
			sendKeyChar('。');
		});

		setInputView(view);
		setCandidatesView(candidates);
	}

	/**
	 * 更新候选字文本
	 */
	private void updateCandidate() {
		switch (inputMode) {
			case HIRAGANA :
				candidate.setText(Dictionary.parseHiragana(input.getText().toString()));
				capslockButton.setText("平假");
				break;
			case KATAKANA :
				candidate.setText(Dictionary.parseKatakana(input.getText().toString()));
				capslockButton.setText("片假");
				break;
		}
	}

	private enum InputMode {
		HIRAGANA, KATAKANA;

		InputMode next() {
			int size = values().length;
			int id = ordinal();
			id += 1;
			if (size == id) {
				return values()[0];
			} else {
				return values()[id];
			}
		}
	}
}
