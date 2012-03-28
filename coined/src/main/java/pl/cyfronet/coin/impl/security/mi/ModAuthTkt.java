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

package pl.cyfronet.coin.impl.security.mi;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class ModAuthTkt {

	private String secret;

	private String modAuthTktDigest(String secret, byte[] data1, byte[] data2)
			throws NoSuchAlgorithmException {

		byte[] part1 = new byte[data1.length + secret.getBytes().length
				+ data2.length];

		ByteBuffer buf1 = ByteBuffer.wrap(part1);
		buf1.order(ByteOrder.BIG_ENDIAN);
		buf1.put(data1);
		buf1.put(secret.getBytes());
		buf1.put(data2);

		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] thedigest0 = md.digest(part1);

		StringBuffer hexdigest0 = new StringBuffer();
		for (int i = 0; i < thedigest0.length; i++) {
			hexdigest0.append(Integer.toString((thedigest0[i] & 0xff) + 0x100,
					16).substring(1));
		}

		byte[] part2 = new byte[hexdigest0.toString().getBytes().length
				+ secret.getBytes().length];

		ByteBuffer buf2 = ByteBuffer.wrap(part2);
		buf2.order(ByteOrder.BIG_ENDIAN);
		buf2.put(hexdigest0.toString().getBytes());
		buf2.put(secret.getBytes());

		byte[] thedigest = md.digest(part2);

		StringBuffer hexdigest = new StringBuffer();
		for (int i = 0; i < thedigest.length; i++) {
			hexdigest.append(Integer
					.toString((thedigest[i] & 0xff) + 0x100, 16).substring(1));
		}
		return hexdigest.toString();
	}

	private String createTicket(String userID, String tokens, String userData,
			String ipAddress, long timestamp /* seconds */) {

		String ticket = new String();
		byte separator = 0;

		if (timestamp == 0) {
			timestamp = new Date().getTime() / 1000;
		}

		try {

			byte[] data1 = new byte[8];
			ByteBuffer buf1 = ByteBuffer.wrap(data1);
			buf1.order(ByteOrder.BIG_ENDIAN);
			buf1.put(InetAddress.getByName(ipAddress).getAddress());
			buf1.putInt((int) timestamp);

			byte[] data2 = new byte[userID.getBytes().length
					+ tokens.getBytes().length + userData.getBytes().length + 2];
			ByteBuffer buf2 = ByteBuffer.wrap(data2);
			buf2.order(ByteOrder.BIG_ENDIAN);
			buf2.put(userID.getBytes());
			buf2.put(separator);
			buf2.put(tokens.getBytes());
			buf2.put(separator);
			buf2.put(userData.getBytes());

			String digest = modAuthTktDigest(secret, data1, data2);

			ticket = String.format("%s%08x%s!", new Object[] { digest,
					timestamp, userID });
			if (tokens != null && tokens != "") {
				ticket += tokens + "!";
			}
			ticket += userData;

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return ticket;
	}

	public TicketAuthStatus validateTicket(String ticket, String ipAddress,
			long timeout /* seconds */, long now /* seconds */) {

		if (ticket.length() <= 40) {
			return TicketAuthStatus.INVALID;
		}

		String digest = ticket.substring(0, 32);
		String val = ticket.substring(32, 40);
		Integer timestamp = Integer.valueOf(val, 16);
		long ltimestamp = timestamp.longValue();

		if (now == 0) {
			now = new Date().getTime() / 1000;
		}

		try {
			UserInfo userInfo = new UserInfo(ticket);

			String newTicket = createTicket(userInfo.getUserId(),
					userInfo.getTokens(), userInfo.getUserData(), ipAddress,
					ltimestamp);

			if (digest.compareTo(newTicket.substring(0, 32)) == 0) {

				if (timeout == 0)
					return TicketAuthStatus.VALID;

				if (ltimestamp + timeout > now) {
					return TicketAuthStatus.VALID;
				} else {
					return TicketAuthStatus.EXPIRED;
				}
			}
		} catch (WrongTicketFormatException e) {
			return TicketAuthStatus.INVALID;
		}
		return TicketAuthStatus.INVALID;
	}

	/**
	 * @param secret the secret to set
	 */
	@Required
	public void setSecret(String secret) {
		this.secret = secret;
	}
}
