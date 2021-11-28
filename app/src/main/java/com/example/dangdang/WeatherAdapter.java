package com.example.dangdang;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class WeatherAdapter extends BaseAdapter {

    private TextView day, hour, temp, pop;
    private ImageView wfkor;

    private ArrayList<WeatherItem> ItemList = new ArrayList<WeatherItem>();


    @Override
    public int getCount() {
        return ItemList.size();
    }

    @Override
    public WeatherItem getItem(int position) {
        return ItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListViewHolder holder;

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item, null);

            holder = new ListViewHolder();

            holder.day = (TextView) convertView.findViewById(R.id.Daytxt) ;
            holder.hour = (TextView) convertView.findViewById(R.id.Hourtxt) ;
            holder.temp = (TextView) convertView.findViewById(R.id.Temptxt) ;
            holder.wfkor = (ImageView) convertView.findViewById(R.id.Wfkorimg) ;
            holder.pop = (TextView) convertView.findViewById(R.id.Poptxt) ;

            convertView.setTag(holder);
        } else {
            holder = (ListViewHolder) convertView.getTag();
        }

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        WeatherItem item = ItemList.get(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        holder.day.setText(item.getDay());
        holder.hour.setText(item.getHour());
        holder.temp.setText(item.getTemp());
        holder.wfkor.setImageDrawable(item.getWfkor());
        holder.pop.setText(item.getPop());

        return convertView;
    }

    public class ListViewHolder{
        TextView day, hour, temp, pop;
        ImageView wfkor;
    }

    public void addItem(WeatherItem item){
        ItemList.add(item);
    }

}