package com.example.appcoches;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private CocheAdapter cocheAdapter;
    private List<Coche> cocheList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // Obtén la referencia a la base de datos
        databaseReference = FirebaseDatabase.getInstance().getReference().child("coches");

        // Inicializa la lista de coches
        cocheList = new ArrayList<>();

        // Inicializa el RecyclerView y el adaptador
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cocheAdapter = new CocheAdapter(this, cocheList);
        recyclerView.setAdapter(cocheAdapter);

        // Llama a la función para buscar y mostrar detalles del coche por nombre
        buscarYMostrarDetalles("sentra");
    }

    private void buscarYMostrarDetalles(String nombreCoche) {
        // Crea una consulta para buscar el coche por nombre
        databaseReference.orderByChild("nombreP").equalTo(nombreCoche)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Recorre los resultados de la consulta
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Coche coche = snapshot.getValue(Coche.class);

                            // Agrega el coche a la lista
                            if (coche != null) {
                                cocheList.add(coche);
                            }
                        }

                        // Notifica al adaptador que los datos han cambiado
                        cocheAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Maneja el error si la búsqueda es cancelada o falla
                    }
                });
    }
}
