package com.cjinks.initialapp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
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
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.function.Function;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class CalendarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

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
        calendarView.setup(currentMonth.minusMonths(10), currentMonth.plusMonths(10), firstDayOfWeek);
        calendarView.scrollToMonth(currentMonth);
    }

    public static class DayViewContainer extends ViewContainer
    {
        public TextView textView;
        public ConstraintLayout layout;
        public CalendarDay day;

        public DayViewContainer(View view) {
            super(view);
            textView = view.findViewById(R.id.exFiveDayText);
            layout = view.findViewById(R.id.exFiveDayLayout);
            view.setOnClickListener(v -> {
                v.findViewById(R.id.exFiveDayFrame).setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.teal_200));
            });
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
