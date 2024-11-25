package com.odipartrack.service;

import com.odipartrack.model.Office;
import com.odipartrack.model.Sale;
import com.odipartrack.model.Envio;
import com.odipartrack.model.Camion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaleService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Llama al Stored Procedure LeerPedidos y devuelve todos los pedidos.
     *
     * @return Lista de objetos Sale.
     */
    public List<Sale> obtenerPedidos() {
        String sql = "CALL LeerPedidos()";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            // Asignar datos básicos del pedido
            Sale sale = new Sale();
            sale.setId(rs.getInt("id"));
            sale.setIdOrigin(rs.getInt("idOrigen"));
            sale.setIdDestination(rs.getInt("idDestino"));
            sale.setQuantity(rs.getInt("Cantidad"));
            sale.setClientId(rs.getString("Cliente"));
            sale.setIdEnvio(rs.getInt("idEnvio"));
            sale.setIdCamion(rs.getInt("idCamion"));
            sale.setDateTime(getLocalDateTime(rs, "Fecha"));

            // Asignar datos de la oficina de origen
            Office origin = new Office();
            origin.setId(rs.getInt("idOrigen"));
            origin.setUbigeo(rs.getString("OrigenUBIGEO"));
            origin.setCapacity(rs.getInt("OrigenCapacidad"));
            origin.setLatitude(rs.getDouble("OrigenLatitud"));
            origin.setLongitude(rs.getDouble("OrigenLongitud"));
            origin.setRegion(rs.getString("OrigenRegion"));
            origin.setDepartment(rs.getString("OrigenDepartamento"));
            origin.setProvince(rs.getString("OrigenProvincia"));
            sale.setOrigin(origin);

            // Asignar datos de la oficina de destino
            Office destination = new Office();
            destination.setId(rs.getInt("idDestino"));
            destination.setUbigeo(rs.getString("DestinoUbigeo"));
            destination.setCapacity(rs.getInt("DestinoCapacidad"));
            destination.setLatitude(rs.getDouble("DestinoLatitud"));
            destination.setLongitude(rs.getDouble("DestinoLongitud"));
            destination.setRegion(rs.getString("DestinoRegion"));
            destination.setDepartment(rs.getString("DestinoDepartamento"));
            destination.setProvince(rs.getString("DestinoProvincia"));
            sale.setDestination(destination);

            // Asignar datos del camión
            Camion camion = new Camion();
            camion.setId(rs.getInt("idCamion"));
            camion.setCodigo(rs.getString("CamionCodigo"));
            camion.setCapacidad(rs.getInt("CamionCapacidad"));
            camion.setTiempoNuevoEnvio(getLocalDateTime(rs, "TiempoNuevoEnvio"));
            camion.setIdInicio(rs.getString("OficinaCamionUBIGEO"));

            // Asignar datos de la oficina de origen del camión
            Office oficinaCamion = new Office();
            oficinaCamion.setId(rs.getInt("idOficinaCamion"));
            oficinaCamion.setUbigeo(rs.getString("OficinaCamionUbigeo"));
            oficinaCamion.setCapacity(rs.getInt("OficinaCamionCapacidad"));
            oficinaCamion.setLatitude(rs.getDouble("OficinaCamionLatitud"));
            oficinaCamion.setLongitude(rs.getDouble("OficinaCamionLongitud"));
            oficinaCamion.setRegion(rs.getString("OficinaCamionRegion"));
            oficinaCamion.setDepartment(rs.getString("OficinaCamionDepartamento"));
            oficinaCamion.setProvince(rs.getString("OficinaCamionProvincia"));
            camion.setInicio(oficinaCamion);

            // Asignar datos del envío
            Envio envio = new Envio();
            envio.setId(rs.getInt("idEnvio"));
            envio.setTiempoLlegada(getLocalDateTime(rs, "TiempoLlegada"));
            envio.setTiempoSalida(getLocalDateTime(rs, "TiempoSalida"));
            envio.setCapacidadRestante(rs.getInt("EnvioCapacidadRestante"));
            envio.setIdCamion(rs.getInt("idCamion"));
            envio.setCamion(camion);
            sale.setEnvio(envio);

            return sale;
        });
    }

    /**
     * Método auxiliar para obtener un LocalDateTime de un ResultSet, manejando
     * valores nulos.
     *
     * @param rs         El ResultSet.
     * @param columnName El nombre de la columna.
     * @return LocalDateTime si no es nulo, de lo contrario, null.
     * @throws SQLException Si ocurre un error al leer el ResultSet.
     */
    private LocalDateTime getLocalDateTime(ResultSet rs, String columnName) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnName);
        return (timestamp != null) ? timestamp.toLocalDateTime() : null;
    }

}
