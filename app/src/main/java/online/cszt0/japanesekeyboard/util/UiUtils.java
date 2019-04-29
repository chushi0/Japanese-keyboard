package online.cszt0.japanesekeyboard.util;

import java.lang.reflect.Field;

import online.cszt0.japanesekeyboard.R;

public class UiUtils {
	public static int getResourcesIdByName(String resName) {
		try {
			Class<R.id> idClass = R.id.class;
			Field field = idClass.getField(resName);
			return field.getInt(null);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalArgumentException("Cannot find id whose name is " + resName, e);
		}
	}
}
