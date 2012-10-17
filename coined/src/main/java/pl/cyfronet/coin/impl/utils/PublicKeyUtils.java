/*
 * Copyright 2012 ACC CYFRONET AGH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package pl.cyfronet.coin.impl.utils;

import java.security.MessageDigest;

/**
 * Based on Jsch code (http://www.jcraft.com/jsch).
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class PublicKeyUtils {

	private static String[] chars = { "0", "1", "2", "3", "4", "5", "6", "7",
			"8", "9", "a", "b", "c", "d", "e", "f" };

	private static final byte[] b64 = str2byte("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=");

	public static String getFingerprint(String publicKeyContent) {
		try {
			byte[] data = unpackPublicKey(publicKeyContent.getBytes());
			MessageDigest hash = MessageDigest.getInstance("MD5");
			hash.reset();
			hash.update(data, 0, data.length);
			byte[] foo = hash.digest();
			StringBuffer sb = new StringBuffer();
			int bar;
			for (int i = 0; i < foo.length; i++) {
				bar = foo[i] & 0xff;
				sb.append(chars[(bar >>> 4) & 0xf]);
				sb.append(chars[(bar) & 0xf]);
				if (i + 1 < foo.length)
					sb.append(":");
			}
			return sb.toString();
		} catch (Exception e) {
			return "???";
		}
	}

	public static byte[] unpackPublicKey(byte[] buf) {
		int i;
		int start;
		int len = buf.length;
		if (buf.length > 4
				&& // FSecure's public key
				buf[0] == '-' && buf[1] == '-' && buf[2] == '-'
				&& buf[3] == '-') {

			boolean valid = true;
			i = 0;
			do {
				i++;
			} while (buf.length > i && buf[i] != 0x0a);
			if (buf.length <= i) {
				valid = false;
			}

			while (valid) {
				if (buf[i] == 0x0a) {
					boolean inheader = false;
					for (int j = i + 1; j < buf.length; j++) {
						if (buf[j] == 0x0a)
							break;
						if (buf[j] == ':') {
							inheader = true;
							break;
						}
					}
					if (!inheader) {
						i++;
						break;
					}
				}
				i++;
			}
			if (buf.length <= i) {
				valid = false;
			}

			start = i;
			while (valid && i < len) {
				if (buf[i] == 0x0a) {
					System.arraycopy(buf, i + 1, buf, i, len - i - 1);
					len--;
					continue;
				}
				if (buf[i] == '-') {
					break;
				}
				i++;
			}
			if (valid) {
				return fromBase64(buf, start, i - start);
			}
		} else {
			if (buf[0] == 's' && buf[1] == 's' && buf[2] == 'h'
					&& buf[3] == '-') {
				i = 0;
				while (i < len) {
					if (buf[i] == ' ')
						break;
					i++;
				}
				i++;
				if (i < len) {
					start = i;
					while (i < len) {
						if (buf[i] == ' ')
							break;
						i++;
					}
					return fromBase64(buf, start, i - start);
				}
			}
		}
		return null;
	}

	private static byte[] fromBase64(byte[] buf, int start, int length) {
		byte[] foo = new byte[length];
		int j = 0;
		for (int i = start; i < start + length; i += 4) {
			foo[j] = (byte) ((val(buf[i]) << 2) | ((val(buf[i + 1]) & 0x30) >>> 4));
			if (buf[i + 2] == (byte) '=') {
				j++;
				break;
			}
			foo[j + 1] = (byte) (((val(buf[i + 1]) & 0x0f) << 4) | ((val(buf[i + 2]) & 0x3c) >>> 2));
			if (buf[i + 3] == (byte) '=') {
				j += 2;
				break;
			}
			foo[j + 2] = (byte) (((val(buf[i + 2]) & 0x03) << 6) | (val(buf[i + 3]) & 0x3f));
			j += 3;
		}
		byte[] bar = new byte[j];
		System.arraycopy(foo, 0, bar, 0, j);
		return bar;
	}

	private static byte val(byte foo) {
		if (foo == '=')
			return 0;
		for (int j = 0; j < b64.length; j++) {
			if (foo == b64[j])
				return (byte) j;
		}
		return 0;
	}

	private static byte[] str2byte(String str, String encoding) {
		if (str == null)
			return null;
		try {
			return str.getBytes(encoding);
		} catch (java.io.UnsupportedEncodingException e) {
			return str.getBytes();
		}
	}

	private static byte[] str2byte(String str) {
		return str2byte(str, "UTF-8");
	}
}
