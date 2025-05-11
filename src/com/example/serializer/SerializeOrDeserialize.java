package com.example.serializer;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

public class SerializeOrDeserialize {
	
		// 針對array反序列化的處理
		// deserializeArray(序列化結果,元素類型,array長度,前綴(key))
		// 成功：return T
		// 失敗：return null
		@SuppressWarnings("unchecked")
		private <T> T deserializeArray(Map<String, String> dataMap, Class<T> componmentType, int length,
				String prefix) {
			Object array = Array.newInstance(componmentType, length);
			for (int i = 0; i < length; i++) {
				try {
					//Array.set(Object array, int index, Object value)(value: 必須是與array元素類型相容的對象)
					Array.set(array, i, deserializeObject(dataMap, componmentType, prefix + "." + i));
				} catch (ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
					return null;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
			return (T) array;
		}
	
		// 字段過濾器
		private boolean filter(Field field) {
			int mod = field.getModifiers();
			
			// 如果字段是枚舉，不過濾
		    if (field.getDeclaringClass().isEnum()) {
		        return false;
		    }
		    
		    // 過濾Static/Transient/Final/Private
			return  Modifier.isStatic(mod) || Modifier.isTransient(mod) ||
					Modifier.isFinal(mod) || Modifier.isPrivate(mod);
		}
	
		// 檢查是否為基本類型包裝類別
		private boolean isWrapperType(Class<?> clazz) {
			return clazz.equals(Integer.class) || clazz.equals(Double.class) || clazz.equals(Float.class)
					|| clazz.equals(Boolean.class) || clazz.equals(Long.class) || clazz.equals(Short.class)
					|| clazz.equals(Byte.class) || clazz.equals(Character.class);
		}
		
	    // 序列化
		public void serializeObject(Object obj, List<String> serializedData, String prefix) {
			
			// 過濾空物件
			if (obj == null) {
				return;
			}

			// 所有字段都處理
			for (Field field : obj.getClass().getFields()) {
				// 打開私有字段存取權
				field.setAccessible(true);
				
				// 字段的值
				Object value = null;
				try {
					value = field.get(obj);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
				
				// 字串串接
				String key = prefix.isEmpty() ? field.getName() : prefix + "." + field.getName();

				// 過濾特殊字段或修飾子
				if (filter(field)) {
					continue;
				} else {
					if (value == null) {
					    serializedData.add(key + "=null");
					// 是array
					} else if (value.getClass().isArray()) {
						int length = Array.getLength(value);
						// 有給註解
						if (field.isAnnotationPresent(ArrayDesc.class)) {
							ArrayDesc arrayDesc = field.getAnnotation(ArrayDesc.class);
							serializedData.add(arrayDesc.lengthVarName() + "=" + length);
						// 沒給註解
						} else {
							serializedData.add(key + "." + "Length=" + length);
						}
						for (int i = 0; i < length; i++) {
							// Array.get(array, index),回傳Object
							serializeObject(Array.get(value, i), serializedData, key + "." + i);
						}
					// 不是基本類型/string/基本類型包裝類別/enum
					} else if (!field.getType().isPrimitive() && !field.getType().equals(String.class)
							&& !isWrapperType(field.getType()) && !field.getType().isEnum()) {
						serializeObject(value, serializedData, key);
					} 
					// 是基本類型/string/基本類型包裝類/enum
					else {
						serializedData.add(key + "=" + value.toString());
					}
				}
			}
		}
		
		// 反序列化
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public <T> T deserializeObject(Map<String, String> dataMap, Class<T> clazz, String prefix) {
			// ？.class先用getDeclaredConstructor()得到其建構子
			// 再用newInstance()執行建構子創造新的物件
			Object obj;
			try {
				obj = clazz.getDeclaredConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				return null;
			}

			// 遍歷物件字段
			for (Field field : clazz.getFields()) {
				// 打開私有字段存取權
				field.setAccessible(true);
				// 要拿來跟map比對的字串(還原)
				String key = prefix.isEmpty() ? field.getName() : prefix + "." + field.getName();

				// 過濾特殊字段或修飾子
				if (filter(field)) {
					continue;
				}

				// 字串比對完全符合：設值 (是基本型態/字串/基本類型包裝類別/enum)
				if (dataMap.containsKey(key)) {
					String value = dataMap.get(key);
					try {
						if (value.equals("null")) {
		                    field.set(obj, null);
		                } else if (field.getType().equals(int.class)) {
					        field.setInt(obj, Integer.parseInt(value));
					    } else if (field.getType().equals(float.class)) {
					        field.setFloat(obj, Float.parseFloat(value));
					    } else if (field.getType().equals(double.class)) {
					        field.setDouble(obj, Double.parseDouble(value));
					    } else if (field.getType().equals(boolean.class)) {
					        field.setBoolean(obj, Boolean.parseBoolean(value));
					    } else if (field.getType().equals(long.class)) {
					        field.setLong(obj, Long.parseLong(value));
					    } else if (field.getType().equals(short.class)) {
					        field.setShort(obj, Short.parseShort(value));
					    } else if (field.getType().equals(byte.class)) {
					        field.setByte(obj, Byte.parseByte(value));
					    } else if (field.getType().equals(char.class)) {
					        field.setChar(obj, value.charAt(0));
					    } else if (field.getType().equals(String.class)) {
					        field.set(obj, value);
					    } else if (field.getType().equals(Integer.class)) {
					        field.set(obj, Integer.valueOf(value));
					    } else if (field.getType().equals(Float.class)) {
					        field.set(obj, Float.valueOf(value));
					    } else if (field.getType().equals(Double.class)) {
					        field.set(obj, Double.valueOf(value));
					    } else if (field.getType().equals(Boolean.class)) {
					        field.set(obj, Boolean.valueOf(value));
					    } else if (field.getType().equals(Long.class)) {
					        field.set(obj, Long.valueOf(value));
					    } else if (field.getType().equals(Short.class)) {
					        field.set(obj, Short.valueOf(value));
					    } else if (field.getType().equals(Byte.class)) {
					        field.set(obj, Byte.valueOf(value));
					    } else if (field.getType().equals(Character.class)) {
					        field.set(obj, value.charAt(0));
					    } else if (field.getType().isEnum()) {
					        field.set(obj, Enum.valueOf((Class<Enum>) field.getType(), value));
					    }
					} catch (IllegalAccessException | IllegalArgumentException e) {
					    e.printStackTrace();
					    return null;
					}

				// 如果是array
				} else if (field.getType().isArray()) {
					int length;
					// 有註解
					if (field.isAnnotationPresent(ArrayDesc.class)) {
						// 建立一個註解物件把註解傳進去
						ArrayDesc arrayDesc = field.getAnnotation(ArrayDesc.class);
						// 比對map中的鍵值並得到陣列長度,強制轉型存入length
						length = Integer.parseInt(dataMap.get(arrayDesc.lengthVarName()));
						// 沒註解
					} else {
						// 比對map中的鍵值並得到陣列長度,強制轉型存入length
						length = Integer.parseInt(dataMap.get(key + "." + "Length"));
					}
					Object array = deserializeArray(dataMap, field.getType().getComponentType(), length, key);
					try {
						field.set(obj, array);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
						return null;
					}
					// 不是基本類型/string/基本類型包裝類別/enum
				} else if (!field.getType().isPrimitive() && !field.getType().equals(String.class)
						&& !isWrapperType(field.getType()) && !field.getType().isEnum()) {
					Object nestObject = deserializeObject(dataMap, field.getType(), key);
					try {
						field.set(obj, nestObject);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
						return null;
					}
				} else {
					System.out.println("Important parameters " + key + " are missing,"
							+ "please check the ini file or reserialize!");
					System.exit(1);
				}
			}
			return (T) obj;
		}
}
