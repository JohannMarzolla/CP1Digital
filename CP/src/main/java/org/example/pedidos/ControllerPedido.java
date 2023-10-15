package org.example.pedidos;


import org.example.produtos.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class ControllerPedido {


    @Autowired
    private ServicePedido servicePedido;
    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestBody Pedido pedido) {

        Double total = calcularTotalPedido(pedido);
        Double totalComTaxa = total + (total * 0.0535);
        pedido.setPreco(totalComTaxa);
        Pedido pedidoSalvo = servicePedido.salvarPedido(pedido);
        return ResponseEntity.ok(pedidoSalvo);
    }


    private Double calcularTotalPedido(Pedido pedido) {
        Double total = 0.0;
        for (Produto produto : pedido.getItensPedidos()) {
            total += produto.getPreco();
        }
        return total;
    }


    @GetMapping()
    public ResponseEntity<List<Pedido>> buscarPedidos() {

        return ResponseEntity.ok(servicePedido.buscarTodosPedidos());
    }

    @PutMapping("/{id}")
    public  ResponseEntity<Pedido> updatePedido(@PathVariable Long id,@RequestBody Pedido pedido){
        if (servicePedido.encontarPedidoPeloId(id)==null){
            return ResponseEntity.notFound().build();
        }
        pedido.setId(id);
        return ResponseEntity.ok(servicePedido.salvarPedido(pedido));

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Long id) {
        if (servicePedido.encontarPedidoPeloId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        servicePedido.deletarPedidoPeloId(id);
        return ResponseEntity.noContent().build();
    }

}



