package com.cjinks.initialapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.CalendarMonth;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder;
import com.kizitonwose.calendarview.ui.ViewContainer;

import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CalendarView calendarView = findViewById(R.id.exOneCalendar);
        calendarView.setDayBinder(new DayBinder<DayViewContainer>() {
            @Override
            public DayViewContainer create(View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(DayViewContainer viewContainer, CalendarDay calendarDay) {
                viewContainer.textView.setText(String.valueOf(calendarDay.getDate().getDayOfMonth()));
            }
        });
        calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<MonthHeaderContainer>() {
            @Override
            public MonthHeaderContainer create(View view) {
                return new MonthHeaderContainer(view);
            }

            @Override
            public void bind(MonthHeaderContainer viewContainer, CalendarMonth calendarMonth) {
                viewContainer.textView.setText(new DateFormatSymbols().getMonths()[calendarMonth.getMonth()-1]);
            }
        });

        YearMonth currentMonth = YearMonth.now();
        YearMonth firstMonth = currentMonth.minusMonths(10);
        YearMonth lastMonth = currentMonth.plusMonths(10);
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek);
        calendarView.scrollToMonth(currentMonth);
    }

    public static class DayViewContainer extends ViewContainer
    {
        public TextView textView;

        public DayViewContainer(View view) {
            super(view);
            textView = view.findViewById(R.id.exTwoDayText);
        }
    }

    public static class MonthHeaderContainer extends ViewContainer {

        public TextView textView;
        public MonthHeaderContainer(View view) {
            super(view);
            textView = view.findViewById(R.id.exTwoHeaderText);
        }
    }
}
