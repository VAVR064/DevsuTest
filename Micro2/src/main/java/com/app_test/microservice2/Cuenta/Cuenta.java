package com.app_test.microservice2.Cuenta;

import com.app_test.microservice2.Cliente.Cliente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cuenta")
public class Cuenta {
    @Id
    @Column(name = "idcuenta", nullable = false)
    private int idCuenta;

    @ManyToOne
    @JoinColumn(name = "clienteid")
    private Cliente clienteId;

    @Column(name = "Tipo", nullable = false)
    private String tipo;

    @Column(name = "Saldo", nullable = false)
    private double saldo;

    @Column(name = "Estado", nullable = false)
    private boolean estado;

    public Cuenta(int idCuenta){
        this.idCuenta = idCuenta;
    }
}
