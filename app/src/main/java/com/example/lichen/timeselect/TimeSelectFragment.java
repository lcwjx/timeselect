package com.example.lichen.timeselect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 随访时间选择页面
 */
public class TimeSelectFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.cancel)
    TextView cancelBtn;
    @Bind(R.id.confirm)
    TextView confirmBtn;
    @Bind(R.id.year_pick)
    ScrollPickerView yearPickView;
    @Bind(R.id.month_pick)
    ScrollPickerView monthPickView;
    @Bind(R.id.day_pick)
    ScrollPickerView dayPickView;
    @Bind(R.id.animation_layout)
    LinearLayout animationLayout;
    @Bind(R.id.hour_pick)
    ScrollPickerView hourPick;
    @Bind(R.id.minute_pick)
    ScrollPickerView minutePick;
    @Bind(R.id.x_time_unit)
    LinearLayout xTimeUnit;
    @Bind(R.id.x_year)
    TextView xYear;
    @Bind(R.id.x_month)
    TextView xMonth;
    @Bind(R.id.x_day)
    TextView xDay;
    @Bind(R.id.x_hour)
    TextView xHour;
    @Bind(R.id.x_minute)
    TextView xMinute;

    private Date initTime;


    private int year;
    private int month;
    private int day = 1;
    private int hour;
    private int minute;
    /**
     * 最大的字体
     */
    private int maxTestSize;
    /**
     * 最小的字体
     */
    private int minTestSize;

    // 初始月份
    private static final int ORIGIN_YEAR = 2000;
    private static final int ORIGIN_MONTH = 1;

    // 可选择的年份，从1970到现在
    private static final String[] YEARS;

    static {
        ArrayList<String> list = new ArrayList<String>();
        int curYear = DateUtil.getYear();
        for (int i = TimeSelectConfig.yearStart; i <= curYear; i++) {
            list.add(i + "");
        }
        YEARS = list.toArray(new String[list.size()]);
    }

    // 月份
    private static final String[] MONTHS = {"1", "2", "3", "4", "5", "6", "7",
            "8", "9", "10", "11", "12"};
    // 小时
    private static final String[] HOURS = {"0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
    // 分钟
    private static final String[] MINUTES;

    static {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 60; i++) {
            list.add(i + "");
        }
        MINUTES = list.toArray(new String[list.size()]);
    }

    public static class TimeSelectConfig {
        /**
         * 默认.年,月,日,时,分
         */
        public static final int TimeType1 = 1;
        /**
         * 年+月
         */
        public static final int TimeType2 = 2;
        /**
         * 仅有年
         */
        public static final int TimeType3 = 3;
        /**
         * 年＋月＋日
         */
        public static final int TimeType4 = 4;
        /**
         * 单位在数字后面
         */
        public static final int VerticalUnitType = 1;
        /**
         * 单位在头部，默认模式
         */
        public static final int HorizontalUnitType = 2;

        public int unitPlacesType = 2;

        public int timeSelectType = 1;
        /**
         * 设置起始年
         */
        public static int yearStart = 1970;
        /**
         * 是否循环滚动
         */
        public static boolean IsCirculation = true;

    }

    private TimeSelectConfig timeSelectConfig;

    private View animationView;

    public interface TimeSelectInterface {
        void onTimeSelect(Date time);
    }

    private TimeSelectInterface timeSelectInterface;

    public TimeSelectFragment() {
        this.timeSelectConfig = new TimeSelectConfig();
    }

    public TimeSelectFragment(TimeSelectConfig timeSelectConfig) {
        this.timeSelectConfig = timeSelectConfig;

    }


    public void setTimeSelectInterface(TimeSelectInterface selectInterface) {
        this.timeSelectInterface = selectInterface;
    }

    /**
     * 设置默认时间
     *
     * @param time
     */
    public void setInitTime(Date time) {
        initTime = time;
    }

    /**
     * 初始化默认时间
     */
    private void initTimeSelect() {
        if (initTime != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(initTime);
            yearPickView.setSelectedPosition(yearPickView.getData().indexOf(
                    "" + calendar.get(Calendar.YEAR)));
            if (timeSelectConfig.timeSelectType == TimeSelectConfig.TimeType3) {
                return;
            }
            monthPickView.setSelectedPosition(monthPickView.getData().indexOf(calendar.get(Calendar.MONTH) + 1 + ""));
            if (timeSelectConfig.timeSelectType == TimeSelectConfig.TimeType2) {
                return;
            }
            dayPickView.setSelectedPosition(dayPickView.getData().indexOf(calendar.get(Calendar.DAY_OF_MONTH) + ""));
            if (timeSelectConfig.timeSelectType == TimeSelectConfig.TimeType4) {
                return;
            }
            hourPick.setSelectedPosition(hourPick.getData().indexOf(calendar.get(Calendar.HOUR_OF_DAY) + ""));
            minutePick.setSelectedPosition(minutePick.getData().indexOf(calendar.get(Calendar.MINUTE) + ""));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initTextSize();
        View rootView = inflater.inflate(R.layout.select_time_layout, null);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        ButterKnife.bind(this, rootView);
        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        // 设置数据
        yearPickView.setData(new ArrayList<String>(Arrays.asList(YEARS)));
        monthPickView.setData(new ArrayList<String>(Arrays.asList(MONTHS)));
        dayPickView.setData(DateUtil.getMonthDaysArray(ORIGIN_YEAR, ORIGIN_MONTH));
        hourPick.setData(new ArrayList<String>(Arrays.asList(HOURS)));
        minutePick.setData(new ArrayList<String>(Arrays.asList(MINUTES)));
        //设置字体大小
        if (maxTestSize != -1 && minTestSize != -1) {
            yearPickView.setMaxTestSize(maxTestSize);
            yearPickView.setMinTestSize(minTestSize);
            monthPickView.setMaxTestSize(maxTestSize);
            monthPickView.setMinTestSize(minTestSize);
            dayPickView.setMaxTestSize(maxTestSize);
            dayPickView.setMinTestSize(minTestSize);
            hourPick.setMaxTestSize(maxTestSize);
            hourPick.setMinTestSize(minTestSize);
            minutePick.setMaxTestSize(maxTestSize);
            minutePick.setMinTestSize(minTestSize);
        }
        //设置是否循环滚动
        yearPickView.setIsCirculation(TimeSelectConfig.IsCirculation);
        monthPickView.setIsCirculation(TimeSelectConfig.IsCirculation);
        dayPickView.setIsCirculation(TimeSelectConfig.IsCirculation);
        hourPick.setIsCirculation(TimeSelectConfig.IsCirculation);
        minutePick.setIsCirculation(TimeSelectConfig.IsCirculation);

        //设置单位
        if (timeSelectConfig.unitPlacesType == TimeSelectConfig.VerticalUnitType) {
            yearPickView.setUnit("年");
            monthPickView.setUnit("月");
            dayPickView.setUnit("日");
            hourPick.setUnit("时");
            minutePick.setUnit("分");
            xTimeUnit.setVisibility(View.GONE);
        }
        if (timeSelectConfig.unitPlacesType == TimeSelectConfig.HorizontalUnitType){
            xTimeUnit.setVisibility(View.VISIBLE);
        }

        //不同的type，对应控件的显示和隐藏
        if (timeSelectConfig.timeSelectType == TimeSelectConfig.TimeType2) {
            dayPickView.setVisibility(View.GONE);
            hourPick.setVisibility(View.GONE);
            minutePick.setVisibility(View.GONE);
            xDay.setVisibility(View.GONE);
            xHour.setVisibility(View.GONE);
            xMinute.setVisibility(View.GONE);
            return rootView;
        }
        if (timeSelectConfig.timeSelectType == TimeSelectConfig.TimeType3) {
            monthPickView.setVisibility(View.GONE);
            dayPickView.setVisibility(View.GONE);
            hourPick.setVisibility(View.GONE);
            minutePick.setVisibility(View.GONE);
            xMonth.setVisibility(View.GONE);
            xDay.setVisibility(View.GONE);
            xHour.setVisibility(View.GONE);
            xMinute.setVisibility(View.GONE);
            return rootView;
        }
        if (timeSelectConfig.timeSelectType == TimeSelectConfig.TimeType4) {
            hourPick.setVisibility(View.GONE);
            minutePick.setVisibility(View.GONE);
            xHour.setVisibility(View.GONE);
            xMinute.setVisibility(View.GONE);
            return rootView;
        }
        yearPickView.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
            @Override
            public void onSelected(List<String> data, int position) {
                year = Integer.parseInt(data.get(position));

                if (month == 2) {
                    changeMonthDays();
                }
            }
        });
        monthPickView.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
            @Override
            public void onSelected(List<String> data, int position) {
                month = Integer.parseInt(data.get(position));
                changeMonthDays();
            }
        });


        dayPickView.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
            @Override
            public void onSelected(List<String> data, int position) {
                day = Integer.parseInt(data.get(position));
            }
        });

        hourPick.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
            @Override
            public void onSelected(List<String> data, int position) {
                hour = Integer.parseInt(data.get(position));
            }
        });


        minutePick.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
            @Override
            public void onSelected(List<String> data, int position) {
                minute = Integer.parseInt(data.get(position));
            }
        });
        rootView.setOnClickListener(this);

        animationView = rootView.findViewById(R.id.animation_layout);

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(200);
        animationView.setAnimation(animation);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initTimeSelect();

    }

    /**
     * 根据不同的type，设置字体的大小
     */
    private void initTextSize() {
        if (timeSelectConfig.timeSelectType == TimeSelectConfig.TimeType1) {
            maxTestSize = DensityUtil.dip2px(getContext(), 16);
            minTestSize = DensityUtil.dip2px(getContext(), 13);
        } else if (timeSelectConfig.timeSelectType == TimeSelectConfig.TimeType2) {
            maxTestSize = DensityUtil.dip2px(getContext(), 20);
            minTestSize = DensityUtil.dip2px(getContext(), 17);
        } else if (timeSelectConfig.timeSelectType == TimeSelectConfig.TimeType3) {
            maxTestSize = DensityUtil.dip2px(getContext(), 22);
            minTestSize = DensityUtil.dip2px(getContext(), 19);
        } else if (timeSelectConfig.timeSelectType == TimeSelectConfig.TimeType4) {
            maxTestSize = DensityUtil.dip2px(getContext(), 18);
            minTestSize = DensityUtil.dip2px(getContext(), 15);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                getFragmentManager().popBackStack();
                break;
            case R.id.confirm:
                if (timeSelectInterface != null) {
                    makeTimeStringAndCallback();
                }
                getFragmentManager().popBackStack();
                break;
            default:
                getFragmentManager().popBackStack();
                break;

        }
    }

    private void makeTimeStringAndCallback() {
        String timeString = null;
        SimpleDateFormat dateFormat1 = null;
        switch (timeSelectConfig.timeSelectType) {
            case TimeSelectConfig.TimeType1:
                timeString = yearPickView.getSelectedItem() + "年" + monthPickView.getSelectedItem() + "月" + dayPickView.getSelectedItem() + "日" + hourPick.getSelectedItem() + "时" + minutePick.getSelectedItem() + "分";
                dateFormat1 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
                break;
            case TimeSelectConfig.TimeType2:
                timeString = yearPickView.getSelectedItem() + "年" + monthPickView.getSelectedItem() + "月";
                dateFormat1 = new SimpleDateFormat("yyyy年MM月");
                break;
            case TimeSelectConfig.TimeType3:
                timeString = yearPickView.getSelectedItem() + "年";
                dateFormat1 = new SimpleDateFormat("yyyy年");
                break;
            case TimeSelectConfig.TimeType4:
                timeString = yearPickView.getSelectedItem() + "年" + monthPickView.getSelectedItem() + "月" + dayPickView.getSelectedItem() + "日";
                dateFormat1 = new SimpleDateFormat("yyyy年MM月dd日");
                break;
        }

        try {
            timeSelectInterface.onTimeSelect(dateFormat1.parse(timeString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    // 更新天数
    private void changeMonthDays() {
        List<String> dayList = DateUtil.getMonthDaysArray(year, month);
        if (dayList.size() != 0 && day > 0 && dayPickView != null) {
            dayPickView.setData(dayList);
            dayPickView.setSelectedPosition(day > dayList.size() ? dayList
                    .size() - 1 : day - 1);
        }
    }

}
