package online.cszt0.japanesekeyboard;

import android.inputmethodservice.InputMethodService;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import online.cszt0.japanesekeyboard.util.UiUtils;

public class IMEService extends InputMethodService {

	private Button[] alphabetKey;
	private Button deleteButton;

	@Override
	public void onCreate() {
		super.onCreate();
		View view = getLayoutInflater().inflate(R.layout.ime_main, null);
		// findViewsById
		alphabetKey = new Button[26];
		View.OnClickListener alphabetKeyListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Button button = (Button) v;
				String text = button.getText().toString();
				// TODO: 暂时只做输入英文字符
				sendKeyChar(text.charAt(0));
			}
		};
		for (int i = 0; i < 26; i++) {
			char c = (char) ('a' + i);
			String idName = "key_" + c;
			int id = UiUtils.getResourcesIdByName(idName);
			alphabetKey[i] = view.findViewById(id);
			alphabetKey[i].setOnClickListener(alphabetKeyListener);
		}
		deleteButton = view.findViewById(R.id.key_del);
		deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendDownUpKeyEvents(KeyEvent.KEYCODE_DEL);
			}
		});
		setInputView(view);
	}
}
