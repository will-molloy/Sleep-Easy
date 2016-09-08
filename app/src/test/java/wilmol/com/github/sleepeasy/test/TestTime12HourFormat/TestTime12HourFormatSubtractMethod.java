package wilmol.com.github.sleepeasy.test.TestTime12HourFormat;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import wilmol.com.github.sleepeasy.Time12HourFormat;

import static org.junit.Assert.assertEquals;

/**
 * To test the subtract method in Time12HourFormat class.
 * Created by will on 8/09/16.
 */
public class TestTime12HourFormatSubtractMethod {

    @Test
    public void subtract90TwelveTimes() throws Exception {
        List<String> times = new ArrayList<String>();

        int i = 0;
        while (i++ < 12) {
            Time12HourFormat time = new Time12HourFormat(0, 00, true); // 12:00am
            time = time.subtractNinteyMinutesXTimes(i);              // subtract cycle of 90mins
            time = time.subtract(new Time12HourFormat(0, 15, true));   // subtract 15mins
            times.add(time.toString());
        }

        List<String> expect = new ArrayList<String>();
        expect.add("10:15PM");
        expect.add("8:45PM");
        expect.add("7:15PM");
        expect.add("5:45PM");
        expect.add("4:15PM");
        expect.add("2:45PM");

        expect.add("1:15PM");
        expect.add("11:45AM");
        expect.add("10:15AM");
        expect.add("8:45AM");
        expect.add("7:15AM");
        expect.add("5:45AM");

        assertEquals(expect, times);
    }

}
