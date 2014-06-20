package kr.co.adflow.push.handler;

import java.util.GregorianCalendar;

import kr.co.adflow.push.R;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;

/**
 * @author nadir93
 * @date 2014. 6. 19.
 */
public class NotificationHandler {

	public static final String TAG = "노티핸들러";

	private static final String ADDCALENDAR = "캘린더추가";
	private static final String DETAILVIEW = "자세히보기";
	private static final int BIG_PICTURE_STYLE = 0;
	private static final int BIG_TEXT_STYLE = 1;
	private static final int NORMAL_STYLE = 2;
	private static int msgCnt = 0;

	public static void notify(Context context, JSONObject noti)
			throws Exception {
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = null;
		Intent resultIntent = new Intent(context,
				kr.co.adflow.push.MainActivity.class);
		resultIntent.putExtra("image", noti.getString("image"));
		resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// Intent.FLAG_ACTIVITY_NEW_TASK
		// resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
		// | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent intent = PendingIntent.getActivity(context, 0,
				resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		// PendingIntent intent = PendingIntent.getActivity(
		// getApplicationContext(), 0, new Intent(Intent.ACTION_VIEW)
		// .setData(Uri.parse("http://www.adflow.co.kr")), 0);

		// ACTION_INSERT does not work on all phones
		// use Intent.ACTION_EDIT in this case
		// PendingIntent intent2 = PendingIntent.getActivity(
		// getApplicationContext(), 0, new Intent(Intent.ACTION_INSERT)
		// .setData(CalendarContract.Events.CONTENT_URI), 0);

		PendingIntent addCalPendingIntent = null;
		Log.d(TAG, "event=" + noti.has("event"));
		if (noti.has("event")) {
			JSONObject event = noti.getJSONObject("event");
			addCalPendingIntent = PendingIntent.getActivity(context, 0,
					makeAddCalIntent(event), PendingIntent.FLAG_UPDATE_CURRENT);
		}

		byte[] data = Base64.decodeBase64(noti.getString("image").getBytes());
		Bitmap bmBigPicture = BitmapFactory.decodeByteArray(data, 0,
				data.length);

		int notificationStyle = noti.getInt("notificationStyle");
		Uri alarmSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		switch (notificationStyle) {
		case BIG_PICTURE_STYLE:
			// bmBigPicture = BitmapFactory.decodeResource(getResources(),
			// R.drawable.android_jellybean);
			// byte[] data = message.getPayload();

			// bigPictureStyle
			notification = new Notification.BigPictureStyle(
					new Notification.Builder(context)
							.setSound(alarmSound)
							// .setLights(Color.parseColor("#5858FA"), 500, 500)
							.setLights(Color.BLUE, 500, 500)
							.setContentTitle(noti.getString("contentTitle"))
							.setContentText(noti.getString("contentText"))
							.setSmallIcon(R.drawable.ic_action_event)
							.setLargeIcon(bmBigPicture)
							.setTicker(noti.getString("ticker"))
							.addAction(R.drawable.ic_action_search, DETAILVIEW,
									intent)
							.addAction(R.drawable.ic_action_new_event,
									ADDCALENDAR, addCalPendingIntent)

			).bigPicture(bmBigPicture)
					.setBigContentTitle(noti.getString("contentTitle"))
					.setSummaryText(noti.getString("summaryText")).build();

			break;
		case BIG_TEXT_STYLE:
			// bigTextStyle
			notification = new Notification.BigTextStyle(
					new Notification.Builder(context).setSound(alarmSound)
							.setLights(Color.BLUE, 500, 500)
							.setContentTitle(noti.getString("contentTitle"))
							.setContentText(noti.getString("contentText"))
							.setSmallIcon(R.drawable.icon)
							.setTicker(noti.getString("ticker"))
							.setLargeIcon(bmBigPicture)
							.setLargeIcon(bmBigPicture)).bigText(
					noti.getString("contentText")).build();
			break;
		case NORMAL_STYLE:

			String contentUri = noti.getString("contentUri");

			PendingIntent detailIntent = null;

			if (contentUri != null) {
				detailIntent = PendingIntent.getActivity(context, 0,
						new Intent(Intent.ACTION_VIEW).setData(Uri
								.parse(contentUri)), 0);
			}

			notification = new Notification.Builder(context)
					.setSound(alarmSound).setLights(Color.BLUE, 500, 500)
					.setContentTitle(noti.getString("contentTitle"))
					.setContentText(noti.getString("contentText"))
					.setLargeIcon(bmBigPicture).setContentIntent(detailIntent)
					.setTicker(noti.getString("ticker"))
					.setSmallIcon(R.drawable.icon).build();
		default:

		}

		// notification.ledARGB = Color.YELLOW;
		manager.notify(msgCnt++, notification);
	}

	private static Intent makeAddCalIntent(JSONObject event) throws Exception {

		Log.d(TAG, "makeAddCalIntent시작(event=" + event + ")");
		Intent addCalIntent = new Intent(Intent.ACTION_INSERT);
		addCalIntent.setType("vnd.android.cursor.item/event");
		addCalIntent.putExtra(Events.TITLE, event.getString("title"));
		addCalIntent.putExtra(Events.EVENT_LOCATION,
				event.getString("location"));
		addCalIntent.putExtra(Events.DESCRIPTION, event.getString("desc"));

		// Setting dates
		GregorianCalendar calDate = new GregorianCalendar(event.getInt("year"),
				event.getInt("month"), event.getInt("day"));
		addCalIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
				calDate.getTimeInMillis());
		addCalIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
				calDate.getTimeInMillis());

		// make it a full day event
		// addCalIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

		// make it a recurring Event
		// intent2.putExtra(Events.RRULE,
		// "FREQ=WEEKLY;COUNT=11;WKST=SU;BYDAY=TU,TH");

		// Making it private and shown as busy
		// addCalIntent.putExtra(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);
		// addCalIntent.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
		Log.d(TAG, "makeAddCalIntent종료(intent=" + addCalIntent + ")");
		return addCalIntent;
	}
}
