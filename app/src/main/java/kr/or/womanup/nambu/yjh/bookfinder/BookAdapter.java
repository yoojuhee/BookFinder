package kr.or.womanup.nambu.yjh.bookfinder;


import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    Context context;
    ArrayList<Book> books;
    int layout;

    public BookAdapter(Context context, int layout) {
        this.context = context;
        this.layout = layout;
        books = new ArrayList<>();
    }

    public void addItem(Book book) {
        books.add(book);
    }

    public void clear() {
        books.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(layout, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);
        /* holder.imageView.setImageResource(book.thumbnail);*/
        Glide.with(holder.itemView.getContext()).load(book.thumbnail).into(holder.imageView);
        holder.txtTitle.setText(book.titile);
        holder.txtAuthor.setText(book.authors);
        holder.txtPublisher.setText(book.publisher);
        holder.txtStatus.setText(book.status);
        holder.txtPrice.setText(book.price + "");
        holder.txtSalePrice.setText(book.sale_price + "");
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtAuthor;
        TextView txtPublisher;
        TextView txtStatus;
        TextView txtPrice;
        TextView txtSalePrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView_item);
            txtTitle = itemView.findViewById(R.id.txt_title_item);
            txtAuthor = itemView.findViewById(R.id.txt_author_item);
            txtPublisher = itemView.findViewById(R.id.txt_publisher_item);
            txtStatus = itemView.findViewById(R.id.txt_status_item);
            txtPrice = itemView.findViewById(R.id.txt_price_item);
            txtPrice.setPaintFlags(txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            txtSalePrice = itemView.findViewById(R.id.txt_sale_price_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Book book = books.get(pos);
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("book", book);
                    /* intent.putExtra("url", book.url);*/
                    context.startActivity(intent);

                }
            });
        }
    }
}




