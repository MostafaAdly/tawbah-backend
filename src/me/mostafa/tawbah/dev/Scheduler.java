package me.mostafa.tawbah.dev;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import lombok.Data;
import me.mostafa.tawbah.Tawbah;
import me.mostafa.tawbah.dev.constructors.Azkar;
import me.mostafa.tawbah.dev.constructors.Message;

@Data
public class Scheduler {

	public TimerCalculater calculater = new TimerCalculater();
	private UNIT unit = UNIT.SECOND;
	private Random random = new Random();
	private int highestChance = 500;

	public Scheduler() {
		startTimer();
	}

	private void startTimer() {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				int currentHour = calculater.getCurrentHours();
				ArrayList<Azkar> success = new ArrayList<>();
				for (Azkar zkr : Tawbah.global.azkar.values())
					if (zkr.getActiveHours().contains(currentHour) && zkr.hasChance(random.nextInt(highestChance + 1)))
						success.add(zkr);
				if (!success.isEmpty()) {
					Azkar zkr = success.get(random.nextInt(success.size()));
					if (!zkr.getVerses().isEmpty() && Tawbah.global.channels.containsKey(zkr.getType()))
						Tawbah.global.channels.get(zkr.getType())
								.sendMessage(new Message(zkr.getVerses().get(random.nextInt(zkr.getVerses().size()))));
				}
			}
		}, 1000, 1000l * calculater.multiplier());

	}

	public class TimerCalculater {

		public boolean canSendRightNow(ArrayList<Integer> activeHours) {
			return activeHours.contains(getCurrentHours());
		}

		public int getCurrentHours() {
			return LocalDateTime.now().getHour();
		}

		public ArrayList<Integer> Load_TimePeriods(String period) {
			ArrayList<Integer> list = new ArrayList<>();
			for (String time : period.contains(",") ? period.split(",") : new String[] { period }) { // for [6-10,12-14]
				if (!isInt(time)) {
					if (time.contains("-") && isInt(time.split("-")[0]) && isInt(time.split("-")[1]))
						for (int h = getInt(time.split("-")[0]); h <= getInt(time.split("-")[1]); h++)
							if (!list.contains(h)) // for [6-10]
								list.add(h);
				} else if (!list.contains(getInt(time))) // for [6, 10, 18]
					list.add(getInt(time));
			}
			return list;
		}

		private int getInt(String s) {
			try {
				return Integer.parseInt(s);
			} catch (Exception e) {
				return -1;
			}
		}

		private boolean isInt(String s) {
			try {
				Integer.parseInt(s);
				return true;
			} catch (Exception e) {
				return false;
			}
		}

		private long multiplier() {
			return unit == UNIT.SECOND ? 1
					: (unit == UNIT.MINUTE ? 60
							: (unit == UNIT.HOUR ? 60 * 60
									: (unit == UNIT.DAY ? 60 * 60 * 24
											: (unit == UNIT.WEEK ? 60 * 60 * 24 * 7l
													: (unit == UNIT.MONTH ? 60 * 60 * 24 * 30l
															: (unit == UNIT.YEAR ? 60 * 60 * 24 * 365l : 1))))));
		}
	}

}
