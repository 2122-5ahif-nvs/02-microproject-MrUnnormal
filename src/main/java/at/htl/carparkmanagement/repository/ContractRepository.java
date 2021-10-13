package at.htl.carparkmanagement.repository;

import at.htl.carparkmanagement.entity.Contract;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;

@ApplicationScoped
public class ContractRepository implements PanacheRepository<Contract> {

    @Inject
    ParkingspotRepository parkingspotRepository;

    @Inject
    CustomerRepository customerRepository;

    @Transactional
    public Contract find(Long id) {
        try {
            return this.findById(id);
        } catch (NotFoundException e) {
            return null;
        }
    }

    @Transactional
    public Contract add(Contract contract) {
        if(contract.getId() != null) {
            return null;
        }
        parkingspotRepository.add(contract.getParkingspot());
        customerRepository.add(contract.getCustomer());
        this.persist(contract);
        return contract;
    }

    @Transactional
    public Contract update(Contract contract) {
        if(contract.getId() != null) {
            Contract update = this.find(contract.getId());
            update.setParkingspot(contract.getParkingspot());
            update.setCustomer(contract.getCustomer());
            update.setEndDate(contract.getEndDate());
            update.setStartDate(contract.getStartDate());
            update.setPayDate(contract.getPayDate());
            return update;
        }
        return this.add(contract);
    }

    @Transactional
    public boolean deleteContract(Long id) {
        Contract delete = this.find(id);
        if(delete == null) { return false; }
        this.delete(delete);
        return true;
    }
}
