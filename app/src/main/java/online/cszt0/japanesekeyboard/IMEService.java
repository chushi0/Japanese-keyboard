package online.cszt0.japanesekeyboard;

import android.inputmethodservice.InputMethodService;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import online.cszt0.japanesekeyboard.util.Dictionary;
import online.cszt0.japanesekeyboard.util.UiUtils;

public class IMEService extends InputMethodService {

	private TextView input;
	private TextView candidate;

	private Button[] alphabetKey;
	private Button deleteButton;
	private Button spaceButton;
	private Button enterButton;

	@Override
	public void onCreate() {
		super.onCreate();

		View view = getLayoutInflater().inflate(R.layout.ime_main, null);
		// findViewsById
		input = view.findViewById(R.id.input);
		candidate = view.findViewById(R.id.candidate);
		candidate.setOnClickListener(v -> {
			String text = candidate.getText().toString();
			if (!text.isEmpty()) {
				char[] chars = text.toCharArray();
				for (char c : chars) {
					sendKeyChar(c);
				}
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
			candidate.setText(Dictionary.parseHiragana(input.getText().toString()));
			enterButton.setText("×");
		};
		for (int i = 0; i < 26; i++) {
			char c = (char) ('a' + i);
			String idName = "key_" + c;
			int id = UiUtils.getResourcesIdByName(idName);
			alphabetKey[i] = view.findViewById(id);
			alphabetKey[i].setOnClickListener(alphabetKeyListener);
		}
		deleteButton = view.findViewById(R.id.key_del);
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
					candidate.setText(Dictionary.parseHiragana(text));
				}
			}
		});
		spaceButton = view.findViewById(R.id.key_space);
		spaceButton.setOnClickListener(v -> {
			String text = candidate.getText().toString();
			if (text.isEmpty()) {
				sendKeyChar(' ');
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

		setInputView(view);
	}
}
