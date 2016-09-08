package wilmol.com.github.sleepeasy.test.TestTime12HourFormat;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import wilmol.com.github.sleepeasy.Time12HourFormat;

import static org.junit.Assert.assertEquals;

/**
 * Tests the Time12HourFormat class.
 *
 * Note: incomplete, however works for what I need (the Time12HourFormat class could contain no faults,
 * but I can't confirm that).
 *
 * @author will 2016-09-05
 */
public class TestTime12HourFormatAddMethod {

    @Test
    public void addBothAM() throws Exception {
        Time12HourFormat time1 = new Time12HourFormat(3, 30, true);
        Time12HourFormat time2 = new Time12HourFormat(4, 30, true);

        String result = time1.add(time2).toString();
        assertEquals("8:00AM", result);
    }

    @Test
    public void addBothPM() throws Exception {
        Time12HourFormat time1 = new Time12HourFormat(3, 30, false);
        Time12HourFormat time2 = new Time12HourFormat(4, 30, false);

        String result = time1.add(time2).toString();
        assertEquals("8:00AM", result);
    }

    @Test
    public void pmAndAm() throws Exception {
        Time12HourFormat time1 = new Time12HourFormat(3, 30, false);
        Time12HourFormat time2 = new Time12HourFormat(4, 30, true);

        String result = time1.add(time2).toString();
        assertEquals("8:00PM", result);
    }

    @Test
    public void bothAMNonZeroMinute() throws Exception {
        Time12HourFormat time1 = new Time12HourFormat(9, 30, true);
        Time12HourFormat time2 = new Time12HourFormat(7, 37, true);

        String result = time1.add(time2).toString();
        assertEquals("5:07PM", result);
    }

    @Test
    public void oneAMonePM() throws Exception {
        Time12HourFormat time1 = new Time12HourFormat(9, 30, true);
        Time12HourFormat time2 = new Time12HourFormat(7, 37, false);

        String result = time1.add(time2).toString();
        assertEquals("5:07AM", result);
    }

    @Test
    public void hour12PlusHour24() throws Exception {
        Time12HourFormat time1 = new Time12HourFormat(12, 00, true); // 12pm (12)
        Time12HourFormat time2 = new Time12HourFormat(12, 00, false); // 12am (24)

        String result = time1.add(time2).toString();
        assertEquals("12:00PM", result);
    }

    @Test
    public void hour12PlusHour12() throws Exception {
        Time12HourFormat time1 = new Time12HourFormat(12, 00, true); // 12pm (12)
        Time12HourFormat time2 = new Time12HourFormat(12, 00, true); // 12pm (12)

        String result = time1.add(time2).toString();
        assertEquals("12:00AM", result);
    }

    @Test
    public void hour24PlusHour24() throws Exception {
        Time12HourFormat time1 = new Time12HourFormat(12, 00, false); // 12am (24)
        Time12HourFormat time2 = new Time12HourFormat(12, 00, false); // 12am (24)

        String result = time1.add(time2).toString();
        assertEquals("12:00AM", result);
    }

    // Test add 90 mins
    @Test
    public void add90Am() throws Exception {
        Time12HourFormat time = new Time12HourFormat(7, 00, true);

        String result = time.addNinteyMinutesXTimes(1).toString();
        assertEquals("8:30AM", result);
    }

    @Test
    public void add90Pm() throws Exception {
        Time12HourFormat time = new Time12HourFormat(7, 00, false);

        String result = time.addNinteyMinutesXTimes(1).toString();
        assertEquals("8:30PM", result);
    }

    @Test
    public void add90AmToPm() throws Exception {
        Time12HourFormat time = new Time12HourFormat(11, 00, true);

        String result = time.addNinteyMinutesXTimes(1).toString();
        assertEquals("12:30PM", result);
    }

    @Test
    public void add90PmToAm() throws Exception {
        Time12HourFormat time = new Time12HourFormat(11, 00, false);

        String result = time.addNinteyMinutesXTimes(1).toString();
        assertEquals("12:30AM", result);
    }

    @Test
    public void add90STwelveTimes() throws Exception {
        List<String> times = new ArrayList<String>();

        int i = 0;
        while (i++ < 12){
            Time12HourFormat time = new Time12HourFormat(0,15,true); // 12:15am
            time = time.addNinteyMinutesXTimes(i);              // add cycle of 90mins
            time = time.add(new Time12HourFormat(0,15,true));   // add 15mins
            times.add(time.toString());
        }

        List<String> expect = new ArrayList<String>();
        expect.add("2:00AM");
        expect.add("3:30AM");
        expect.add("5:00AM");
        expect.add("6:30AM");
        expect.add("8:00AM");
        expect.add("9:30AM");

        expect.add("11:00AM");
        expect.add("12:30PM");
        expect.add("2:00PM");
        expect.add("3:30PM");
        expect.add("5:00PM");
        expect.add("6:30PM");

        assertEquals(expect, times);
    }
}
