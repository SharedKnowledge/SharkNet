package net.sharksystem.sharknet;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharkfw.system.L;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by j4rvis on 3/20/17.
 */

public abstract class BaseRecyclerViewAdapter<T extends RecyclerView.ViewHolder, S> extends RecyclerView.Adapter<T> {

    private int layout;
    private Class<T> viewHolder;
    private List<S> list = new ArrayList<>();

    protected BaseRecyclerViewAdapter(int layout, Class<T> viewHolder) {
        this.layout = layout;
        this.viewHolder = viewHolder;
    }

    public void setList(List<S> list) {
        this.list = list;
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        try {
            return viewHolder.getDeclaredConstructor(View.class).newInstance(view);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            L.d("Bubbb...", this);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        S item = list.get(position);
        onBinding(holder, item);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected abstract void onBinding(T holder, S item);
}
