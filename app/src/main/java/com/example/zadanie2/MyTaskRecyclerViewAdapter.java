package com.example.zadanie2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.zadanie2.TaskFragment.OnListFragmentInteractionListener;
import com.example.zadanie2.tasks.TaskListContent.Task;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Task} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyTaskRecyclerViewAdapter extends RecyclerView.Adapter <MyTaskRecyclerViewAdapter.ViewHolder> {

    private final List <Task> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyTaskRecyclerViewAdapter(List <Task> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from ( parent.getContext () )
                .inflate ( R.layout.fragment_task, parent, false );
        return new ViewHolder ( view );
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Task task = mValues.get ( position );
        holder.mItem = task;
        holder.mContentView.setText ( task.title );
        final String picPath = task.picPath;
        Context context = holder.mView.getContext ();
        if (picPath!=null && !picPath.isEmpty ())
        {
            if(picPath.contains ( "drawable" ))
            {
                Drawable taskDrawable;
                switch (picPath)
                {
                    case "drawable 1":
                        taskDrawable = context.getResources ().getDrawable ( R.drawable.circle_drawable_green );
                        break;
                    case "drawable 2":
                        taskDrawable = context.getResources ().getDrawable ( R.drawable.circle_drawable_orange );
                        break;
                    case "drawable 3":
                        taskDrawable = context.getResources ().getDrawable ( R.drawable.circle_drawable_red );
                        break;
                        default:
                            taskDrawable = context.getResources ().getDrawable ( R.drawable.circle_drawable_green );
                }
                holder.mItemImageView.setImageDrawable ( taskDrawable );
            }
            else
            {
                Bitmap cameraImage = PicUtils.decodePic ( task.picPath, 60, 60 );
                holder.mItemImageView.setImageBitmap ( cameraImage );
            }
        }
        else
        {
            holder.mItemImageView.setImageDrawable ( context.getResources ().getDrawable ( R.drawable.circle_drawable_green ) );
        }
        holder.mView.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentClickInteraction ( holder.mItem, position );
                }
            }
        } );
        holder.deleteButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                mListener.OnDeleteClick ( position );
            }
        } );
    }

    @Override
    public int getItemCount() {
        return mValues.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mItemImageView;
        public final TextView mContentView;
        public final ImageView deleteButton;
        public Task mItem;

        public ViewHolder(View view) {
            super ( view );
            mView = view;
            mItemImageView = view.findViewById ( R.id.item_image );
            mContentView = view.findViewById ( R.id.content );
            deleteButton = view.findViewById(R.id.imageView);
        }

        @Override
        public String toString() {
            return super.toString () + " '" + mContentView.getText () + "'";
        }
    }
}
