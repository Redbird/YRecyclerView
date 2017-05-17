package cn.yu.yuan;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yukuo on 2016/4/30.
 * 这是一个测试带有侧滑删除的列表适配器
 */
public class DemoSwipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SwipeDate> list = new ArrayList<>();

    public DemoSwipeAdapter(List<SwipeDate> list) {
        this.list = list;
    }

    public void addReFreshData() {
        SwipeDate swipeDate = new SwipeDate();
        swipeDate.name = "添加刷新数据";
        swipeDate.type = 0;
        list.add(0, swipeDate);
        notifyDataSetChanged();
    }

    public void addRLoadMOreData() {
        SwipeDate swipeDate = new SwipeDate();
        swipeDate.name = "添加加载更多数据";
        swipeDate.type = 0;
        list.add(list.size(), swipeDate);
        notifyDataSetChanged();
    }

    /**
     * 删除一个数据的方法
     *
     * @param position 索引
     */
    public void removeData(int position) {

        //TODO  刷新全部数据有bug,因为复用的问题,导致删除最下边的条目,最上边的条目会有滑动按钮关闭的展示
//        list.remove(position);
//        notifyDataSetChanged();
        notifyItemRemoved(position + 1);
        if (position != list.size()) {
            if (position == 0) {
                /**
                 * 删除第一条数据
                 */
                list.remove(position);
                notifyDataSetChanged();
            } else if (position == (list.size() - 1)) {
                /**
                 * 删除最后一条数据
                 */
                list.remove(position);
                notifyItemRangeChanged(position, 0);
            } else {
                /**
                 * 删除中间的数据
                 */
                list.remove(position);
                notifyItemRangeChanged(position + 1, list.size() - position + 1);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = View.inflate(parent.getContext(), R.layout.item_swipe_menu, null);
            return new MySwipeMenuHolder(view);
        } else {
            view = View.inflate(parent.getContext(), R.layout.item_content, null);
            return new MyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            myHolder.item_content.setText(list.get(position).name);
        } else if (holder instanceof MySwipeMenuHolder) {
            final MySwipeMenuHolder myHolders = (MySwipeMenuHolder) holder;
            myHolders.item_content.setText(list.get(position).name + "######" + position);
            myHolders.item_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), list.get(position).name, Toast.LENGTH_SHORT).show();
                }
            });
            myHolders.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myHolders.srl_item.close();
                    removeData(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        private final TextView item_content;

        public MyHolder(View itemView) {
            super(itemView);
            item_content = (TextView) itemView.findViewById(R.id.item_content);
        }
    }

    class MySwipeMenuHolder extends RecyclerView.ViewHolder {
        private final TextView delete;
        private final TextView ok;
        private final TextView item_content;
        private final SwipeLayout srl_item;

        public MySwipeMenuHolder(View itemView) {
            super(itemView);
            item_content = (TextView) itemView.findViewById(R.id.item_content);
            srl_item = (SwipeLayout) itemView.findViewById(R.id.srl_item);
            delete = (TextView) itemView.findViewById(R.id.delete);
            ok = (TextView) itemView.findViewById(R.id.ok);
        }
    }
}
