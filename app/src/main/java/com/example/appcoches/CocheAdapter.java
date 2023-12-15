package com.example.appcoches;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcoches.Coche;
import com.squareup.picasso.Picasso;
import java.util.List;

public class CocheAdapter extends RecyclerView.Adapter<CocheAdapter.CocheViewHolder> {

    private final Context context;
    private final List<Coche> coches;

    public CocheAdapter(Context context, List<Coche> coches) {
        this.context = context;
        this.coches = coches;
    }

    @NonNull
    @Override
    public CocheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_coche, parent, false);
        return new CocheViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CocheViewHolder holder, int position) {
        Coche coche = coches.get(position);
        holder.bind(coche);
    }

    @Override
    public int getItemCount() {
        return coches.size();
    }

    static class CocheViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView nombreTextView;
        private final TextView precioTextView;
        private final TextView descripcionTextView;

        public CocheViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewCoche);
            nombreTextView = itemView.findViewById(R.id.textViewNombre);
            precioTextView = itemView.findViewById(R.id.textViewPrecio);
            descripcionTextView = itemView.findViewById(R.id.textViewDescripcion);

        }

        public void bind(Coche coche) {
            // Aquí asignas los valores del Coche a las vistas
            nombreTextView.setText(coche.getNombreP());
            precioTextView.setText("$" + coche.getPrecioP());
            descripcionTextView.setText(coche.getDescripcionP());

            // Puedes usar una biblioteca como Picasso para cargar imágenes desde la URL
            Picasso.get().load(coche.getUrlImagen()).into(imageView);
        }
    }
}
