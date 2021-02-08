package com.cjinks.initialapp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.CalendarMonth;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder;
import com.kizitonwose.calendarview.ui.ViewContainer;

import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.function.Function;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class CalendarActivity extends AppCompatActivity {

    private HashMap<LocalDate, Integer> mData;
    private ArrayList<Integer> mColors;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        mData = new HashMap<>();
        mData.put(LocalDate.parse("2021-01-01"), 1);
        mData.put(LocalDate.parse("2021-01-02"), 1);
        mData.put(LocalDate.parse("2021-01-03"), 7);
        mData.put(LocalDate.parse("2021-01-04"), 3);
        mData.put(LocalDate.parse("2021-01-05"), 4);
        mData.put(LocalDate.parse("2021-01-06"), 9);
        mData.put(LocalDate.parse("2021-01-07"), 5);
        mData.put(LocalDate.parse("2021-01-08"), 3);
        mData.put(LocalDate.parse("2021-01-09"), 8);
        mData.put(LocalDate.parse("2021-01-10"), 3);
        mData.put(LocalDate.parse("2021-01-11"), 1);
        mData.put(LocalDate.parse("2021-01-12"), 9);
        mData.put(LocalDate.parse("2021-01-13"), 6);

        mData.put(LocalDate.parse("2021-02-01"), 3);
        mData.put(LocalDate.parse("2021-02-02"), 8);
        mData.put(LocalDate.parse("2021-02-03"), 9);
        mData.put(LocalDate.parse("2021-02-04"), 0);
        mData.put(LocalDate.parse("2021-02-05"), 5);

        mColors = new ArrayList<>();
        mColors.add(ContextCompat.getColor(this, R.color.gradient_0));
        mColors.add(ContextCompat.getColor(this, R.color.gradient_1));
        mColors.add(ContextCompat.getColor(this, R.color.gradient_2));
        mColors.add(ContextCompat.getColor(this, R.color.gradient_3));
        mColors.add(ContextCompat.getColor(this, R.color.gradient_4));
        mColors.add(ContextCompat.getColor(this, R.color.gradient_5));
        mColors.add(ContextCompat.getColor(this, R.color.gradient_6));
        mColors.add(ContextCompat.getColor(this, R.color.gradient_7));
        mColors.add(ContextCompat.getColor(this, R.color.gradient_8));
        mColors.add(ContextCompat.getColor(this, R.color.gradient_9));

        Context context = this;
        CalendarView calendarView = findViewById(R.id.exFiveCalendar);
        calendarView.setDayBinder(new DayBinder<DayViewContainer>() {
            @Override
            public DayViewContainer create(View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(DayViewContainer viewContainer, CalendarDay calendarDay) {
                viewContainer.textView.setText(String.valueOf(calendarDay.getDate().getDayOfMonth()));
                viewContainer.textView.setTextColor(ContextCompat.getColor(context, R.color.example_5_text_grey_light));
                viewContainer.day = calendarDay;
                viewContainer.layout.setBackground(null);

                LocalDate date = calendarDay.getDate();
                viewContainer.listener = () -> {
                    // If the date is present in the data we add to its value, otherwise add the date with a starting value of 0
                    if(mData.containsKey(date)) {
                        mData.put(date, (mData.get(date)+1) % 10);
                    }
                    else {
                        mData.put(date, 0);
                    }
                    viewContainer.layout.setBackgroundColor(mColors.get(mData.get(date)));
                };

                if(mData.containsKey(calendarDay.getDate())) {
                    viewContainer.layout.setBackgroundColor(mColors.get(mData.get(date)));
                }
                else {
                    viewContainer.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.example_5_item_view_bg_color));
                }
            }
        });

        calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<MonthHeaderContainer>() {
            @Override
            public MonthHeaderContainer create(View view) {
                return new MonthHeaderContainer(view);
            }

            @Override
            public void bind(MonthHeaderContainer viewContainer, CalendarMonth calendarMonth) {
//                viewContainer.textView.setText(new DateFormatSymbols().getMonths()[calendarMonth.getMonth()-1]);

                if (viewContainer.legendLayout.getTag() == null) {
                    viewContainer.legendLayout.setTag(calendarMonth.getMonth());
//                    viewContainer.legendLayout.children.map { it as TextView }.forEachIndexed { index, tv ->
//                            tv.text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
//                                    .toUpperCase(Locale.ENGLISH)
//                        tv.setTextColorRes(R.color.example_5_text_grey)
//                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
//                    }
//                    month.yearMonth
                }
            }
        });

        calendarView.setMonthScrollListener(
                calendarMonth -> {
                    TextView tv = findViewById(R.id.exFiveMonthYearText);
                    String title = new DateFormatSymbols().getMonths()[calendarMonth.getMonth()-1] + " " + calendarMonth.getYear();
                    tv.setText(title);
                    return null;
                }
        );

        ImageView nextMonth = findViewById(R.id.exFiveNextMonthImage);
        nextMonth.setOnClickListener(v -> {
            calendarView.smoothScrollToMonth(calendarView.findFirstVisibleMonth().getYearMonth().plusMonths(1));
        });

        ImageView previousMonth = findViewById(R.id.exFivePreviousMonthImage);
        previousMonth.setOnClickListener(v -> {
            calendarView.smoothScrollToMonth(calendarView.findFirstVisibleMonth().getYearMonth().minusMonths(1));
        });

        YearMonth currentMonth = YearMonth.now();
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        calendarView.setup(currentMonth.minusMonths(13), currentMonth.plusMonths(1), firstDayOfWeek);
        calendarView.scrollToMonth(currentMonth);
    }

    public static class DayViewContainer extends ViewContainer
    {
        public TextView textView;
        public FrameLayout layout;
        public CalendarDay day;

        public boolean toggle = true;

        public interface DayClickListener {
            void click();
        }
        DayClickListener listener;

        public DayViewContainer(View view) {
            super(view);
            textView = view.findViewById(R.id.exFiveDayText);
            layout = view.findViewById(R.id.exFiveDayFrame);

            layout.setOnClickListener(v -> { listener.click(); });
        }
    }

    public static class MonthHeaderContainer extends ViewContainer {

        public LinearLayout legendLayout;
        public MonthHeaderContainer(View view) {
            super(view);
            legendLayout = view.findViewById(R.id.legendLayout);
        }
    }
}
