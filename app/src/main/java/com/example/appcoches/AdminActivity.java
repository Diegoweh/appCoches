package com.example.appcoches;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import java.util.Random;


public class AdminActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText txtBusqueda, txtNombre, txtDescripcion, txtPrecio, txtImagen, txtUrl;
    private ImageView imgProducto;
    private CheckBox cbxActivado;
    private Button btnSubir, btnGuardar, btnLimpiar, btnEdiar, btnRegresar, btnBuscar, btnEliminar;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("coches");
        storageReference = FirebaseStorage.getInstance().getReference("imagenes");

        // Inicializar tus componentes de UI
        initUI();

        // Asignar clic listeners a los botones
        btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarDatosEnFirebase();
            }
        });

        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarCampos();
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreBusqueda = txtBusqueda.getText().toString().trim();
                if (!nombreBusqueda.isEmpty()) {
                    buscarCoche(nombreBusqueda);
                } else {
                    Toast.makeText(AdminActivity.this, "Ingrese un nombre para buscar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnEdiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreCoche = txtNombre.getText().toString().trim();
                obtenerCocheIdPorNombre(nombreCoche);
            }
        });

        btnEliminar = findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreCoche = txtNombre.getText().toString().trim();
                if (!nombreCoche.isEmpty()) {
                    eliminarCocheEnFirebase(nombreCoche);
                } else {
                    Toast.makeText(AdminActivity.this, "Ingresa un nombre de coche válido", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para iniciar la actividad MainActivity
                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                // Iniciar la actividad
                startActivity(intent);
                // Cerrar la actividad actual (AdminActivity)
                finish();
            }
        });






    }
    private void initUI() {
        txtBusqueda = findViewById(R.id.txtBusqueda);
        txtNombre = findViewById(R.id.txtNombre);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtPrecio = findViewById(R.id.txtPrecio);
        txtImagen = findViewById(R.id.txtImagen);
        txtUrl = findViewById(R.id.txtUrl);
        cbxActivado = findViewById(R.id.cbxActivado);
        imgProducto = findViewById(R.id.imgProducto);
        btnSubir = findViewById(R.id.btnSubir);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnLimpiar = findViewById(R.id.btnLimpiar);
        btnEdiar = findViewById(R.id.btnEdiar);
        btnRegresar = findViewById(R.id.btnRegresar);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnEliminar = findViewById(R.id.btnEliminar);
    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            imgProducto.setImageURI(imageUri);
        }
    }

    private void guardarDatosEnFirebase() {
        // Obtener los valores de los campos
        String nombre = txtNombre.getText().toString().trim();
        String descripcion = txtDescripcion.getText().toString().trim();
        double precio = Double.parseDouble(txtPrecio.getText().toString().trim());
        String nombreImagen = txtImagen.getText().toString().trim();
        boolean activado = cbxActivado.isChecked();

        Random random = new Random();
        int id = random.nextInt(Integer.MAX_VALUE);

        // Crear una instancia de la clase Coche con los datos obtenidos
        Coche nuevoCoche = new Coche(id, nombre, descripcion, precio, "", nombreImagen, "", activado ? 0 : 1);

        // Obtener una referencia única para el nuevo coche en la base de datos
        String cocheId = databaseReference.push().getKey();

        // Guardar el nuevo coche en la base de datos
        databaseReference.child(cocheId).setValue(nuevoCoche);

        // Guardar la imagen en Firebase Storage
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(nombreImagen);
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Obtener la URL de la imagen en Storage
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String urlImagen = uri.toString();
                            nuevoCoche.setUrlImagen(urlImagen);
                            // Actualizar la URL de la imagen en la base de datos
                            databaseReference.child(cocheId).child("urlImagen").setValue(urlImagen);
                        });
                    })
                    .addOnFailureListener(e -> Toast.makeText(AdminActivity.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show());
        }

        // Limpiar los campos después de guardar
        limpiarCampos();

        // Mostrar un mensaje de éxito
        Toast.makeText(this, "Coche guardado correctamente", Toast.LENGTH_SHORT).show();
    }

    private void limpiarCampos() {
        txtBusqueda.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtImagen.setText("");
        txtUrl.setText("");
        imgProducto.setImageResource(R.drawable.img); // Puedes establecer la imagen por defecto aquí
        cbxActivado.setChecked(false);
    }
    private void buscarCoche(String nombreBusqueda) {
        DatabaseReference cochesRef = FirebaseDatabase.getInstance().getReference().child("coches");

        cochesRef.orderByChild("nombreP").equalTo(nombreBusqueda).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean exito = false;

                for (DataSnapshot cocheSnapshot : dataSnapshot.getChildren()) {
                    Coche coche = cocheSnapshot.getValue(Coche.class);

                    if (coche != null) {
                        // Configurar tus vistas con los datos obtenidos
                        txtNombre.setText(coche.getNombreP());
                        txtDescripcion.setText(coche.getDescripcionP());
                        txtPrecio.setText(String.valueOf(coche.getPrecioP()));
                        txtUrl.setText(coche.getUrlImagen()); // Asegúrate de tener un EditText para el URL
                        // Ajusta la lógica para manejar la imagen según tu necesidad

                        // Marcar que se encontró el coche
                        exito = true;
                        break;
                    }
                }

                if (!exito) {
                    // Limpiar los campos si no se encontró el coche
                    limpiarCampos();
                    Toast.makeText(AdminActivity.this, "Coche no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores, si es necesario
                Toast.makeText(AdminActivity.this, "Error en la consulta", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void editarCocheEnFirebase(String cocheId) {
        // Obtener los valores de los campos
        String nombre = txtNombre.getText().toString().trim();
        String descripcion = txtDescripcion.getText().toString().trim();
        double precio = Double.parseDouble(txtPrecio.getText().toString().trim());
        String nombreImagen = txtImagen.getText().toString().trim();
        String urlImagen = txtUrl.getText().toString().trim();

        // Crear una instancia de la clase Coche con los datos actualizados
        Coche cocheEditado = new Coche(0, nombre, descripcion, precio, "", nombreImagen, urlImagen, 0);

        // Actualizar el coche en la base de datos
        databaseReference.child(cocheId).setValue(cocheEditado);

        // Limpiar los campos después de editar
        limpiarCampos();

        // Mostrar un mensaje de éxito
        Toast.makeText(this, "Coche actualizado correctamente", Toast.LENGTH_SHORT).show();
    }
    private void obtenerCocheIdPorNombre(String nombreCoche) {
        DatabaseReference cochesRef = FirebaseDatabase.getInstance().getReference().child("coches");

        // Realizar una consulta para encontrar el coche con el nombre especificado
        cochesRef.orderByChild("nombreP").equalTo(nombreCoche).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Recorrer los resultados de la consulta
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String cocheId = snapshot.getKey();
                        // Aquí ya tienes el ID del coche con el nombre especificado
                        // Puedes llamar a la función para editar el coche con este ID
                        editarCocheEnFirebase(cocheId);
                        return; // Terminar la función después de encontrar el primer coche con el nombre
                    }
                } else {
                    // No se encontró ningún coche con ese nombre
                    Toast.makeText(AdminActivity.this, "No se encontró ningún coche con ese nombre", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores de la consulta si es necesario
                Toast.makeText(AdminActivity.this, "Error al buscar el coche por nombre", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarCocheEnFirebase(String nombreCoche) {
        DatabaseReference cochesRef = FirebaseDatabase.getInstance().getReference().child("coches");

        // Consulta para encontrar el coche con el nombre especificado
        Query query = cochesRef.orderByChild("nombreP").equalTo(nombreCoche);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot cocheSnapshot : dataSnapshot.getChildren()) {
                    // Obtiene la referencia del coche y lo elimina
                    cocheSnapshot.getRef().removeValue();
                    Toast.makeText(AdminActivity.this, "Coche eliminado correctamente", Toast.LENGTH_SHORT).show();
                    limpiarCampos(); // Limpia los campos después de eliminar el coche
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Maneja errores si la operación es cancelada
                Toast.makeText(AdminActivity.this, "Error al eliminar el coche", Toast.LENGTH_SHORT).show();
            }
        });
    }







}

