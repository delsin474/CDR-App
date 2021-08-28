package com.rakuten.springboot.cdr;

import com.rakuten.springboot.cdr.model.user.Role;
import com.rakuten.springboot.cdr.model.user.User;
import com.rakuten.springboot.cdr.model.user.UserRoles;
import com.rakuten.springboot.cdr.repository.user.RoleRepository;
import com.rakuten.springboot.cdr.repository.user.UserRepository;
import com.rakuten.springboot.cdr.model.cdr.*;
import com.rakuten.springboot.cdr.model.graph.Dijkstra;
import com.rakuten.springboot.cdr.model.graph.Edge;
import com.rakuten.springboot.cdr.model.graph.Vertex;
import com.rakuten.springboot.cdr.model.inmem.CallCategoryM;
import com.rakuten.springboot.cdr.model.inmem.CdrInmemStore;
import com.rakuten.springboot.cdr.model.inmem.ServiceTypeM;
import com.rakuten.springboot.cdr.model.inmem.SubscriberTypeM;
import com.rakuten.springboot.cdr.repository.cdr.*;
import com.rakuten.springboot.cdr.repository.file.FileRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class CdrSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CdrSystemApplication.class, args);
    }

    @Bean
    CommandLineRunner init(RoleRepository roleRepository, UserRepository userRepository, CallCategoryRepository callCategoryRepository,
            ServiceTypeRepository serviceTypeRepository, SubscriberTypeRepository subscriberTypeRepository,
            FileRepository fileRepository, ChargeRepository chargeRepository, CdrRecordRepository cdrRecordRepository ) {
        return args -> {
            

            // Create Admin and Normal Roles
            Role adminRole = roleRepository.findByRole(UserRoles.ADMIN);
            if (adminRole == null) {
                adminRole = new Role();
                adminRole.setRole(UserRoles.ADMIN);
                roleRepository.save(adminRole);
            }

            Role userRole = roleRepository.findByRole(UserRoles.NORMAL_USER);
            if (userRole == null) {
                userRole = new Role();
                userRole.setRole(UserRoles.NORMAL_USER);
                roleRepository.save(userRole);
            }

            // Create an Admin user
            User admin = userRepository.findByEmail("admin@gmail.com");
            if (admin == null) {
                admin = new User().setEmail("admin@gmail.com")
                        .setPassword("$2a$10$7PtcjEnWb/ZkgyXyxY1/Iei2dGgGQUbqIIll/dt.qJ8l8nQBWMbYO") // "123456"
                        .setFirstName("John").setLastName("Doe").setMobileNumber("9425094250")
                        .setRoles(Arrays.asList(adminRole));
                userRepository.save(admin);
            }

            // Create a normal user
            User normal = userRepository.findByEmail("normal@gmail.com");
            if (normal == null) {
                normal = new User().setEmail("normal@gmail.com")
                        .setPassword("$2a$10$7PtcjEnWb/ZkgyXyxY1/Iei2dGgGQUbqIIll/dt.qJ8l8nQBWMbYO") // "123456"
                        .setFirstName("Mira").setLastName("Jane").setMobileNumber("8000110008")
                        .setRoles(Arrays.asList(userRole));
                userRepository.save(normal);
            }

            // Create Call Category
            CallCategory callCategoryLocal = callCategoryRepository.findByCallCategory("Local");
            if (callCategoryLocal == null) {
                callCategoryLocal = new CallCategory().setCallCategory("Local");
                callCategoryRepository.save(callCategoryLocal);
            }

            CallCategory callCategoryRoaming = callCategoryRepository.findByCallCategory("Roaming");
            if (callCategoryRoaming == null) {
                callCategoryRoaming = new CallCategory().setCallCategory("Roaming");
                callCategoryRepository.save(callCategoryRoaming);
            }

            // Create Subscriber Type
            SubscriberType subscriberTypePostpaid = subscriberTypeRepository.findBySubscriberType("Postpaid");
            if (subscriberTypePostpaid == null) {
                subscriberTypePostpaid = new SubscriberType().setSubscriberType("Postpaid");
                subscriberTypeRepository.save(subscriberTypePostpaid);
            }

            SubscriberType subscriberTypePrepaid = subscriberTypeRepository.findBySubscriberType("Prepaid");
            if (subscriberTypePrepaid == null) {
                subscriberTypePrepaid = new SubscriberType().setSubscriberType("Prepaid");
                subscriberTypeRepository.save(subscriberTypePrepaid);
            }

            // Create Voice Type
            ServiceType serviceTypeVoice = serviceTypeRepository.findByServiceType("Voice");
            if (serviceTypeVoice == null) {
                serviceTypeVoice = new ServiceType().setServiceType("Voice");
                serviceTypeRepository.save(serviceTypeVoice);
            }

            ServiceType serviceTypeSMS = serviceTypeRepository.findByServiceType("SMS");
            if (serviceTypeSMS == null) {
                serviceTypeSMS = new ServiceType().setServiceType("SMS");
                serviceTypeRepository.save(serviceTypeSMS);
            }

            ServiceType serviceTypeGPRS = serviceTypeRepository.findByServiceType("GPRS");
            if (serviceTypeGPRS == null) {
                serviceTypeGPRS = new ServiceType().setServiceType("GPRS");
                serviceTypeRepository.save(serviceTypeGPRS);
            }

            // Create Charge Table
            Optional<ServiceType> serviceType1 = serviceTypeRepository.findById(1L);
            Optional<ServiceType> serviceType2 = serviceTypeRepository.findById(2L);
            Optional<ServiceType> serviceType3 = serviceTypeRepository.findById(3L);
            Optional<CallCategory> callCategory1 = callCategoryRepository.findById(1L);
            Optional<CallCategory> callCategory2 = callCategoryRepository.findById(2L);
            Optional<SubscriberType> subscriberType1 = subscriberTypeRepository.findById(1L);
            Optional<SubscriberType> subscriberType2 = subscriberTypeRepository.findById(2L);

            chargeRepository.save(new Charge().setServiceType(serviceType1.get()).setCallCategory(callCategory1.get())
                    .setSubscriberType(subscriberType1.get()).setCharge_usd(0.3));
            chargeRepository.save(new Charge().setServiceType(serviceType1.get()).setCallCategory(callCategory1.get())
                    .setSubscriberType(subscriberType2.get()).setCharge_usd(0.5));
            chargeRepository.save(new Charge().setServiceType(serviceType1.get()).setCallCategory(callCategory2.get())
                    .setSubscriberType(subscriberType1.get()).setCharge_usd(1.5));
            chargeRepository.save(new Charge().setServiceType(serviceType1.get()).setCallCategory(callCategory2.get())
                    .setSubscriberType(subscriberType2.get()).setCharge_usd(2.0));
            chargeRepository.save(new Charge().setServiceType(serviceType2.get()).setCallCategory(callCategory1.get())
                    .setSubscriberType(subscriberType1.get()).setCharge_usd(0.15));
            chargeRepository.save(new Charge().setServiceType(serviceType2.get()).setCallCategory(callCategory1.get())
                    .setSubscriberType(subscriberType2.get()).setCharge_usd(0.25));
            chargeRepository.save(new Charge().setServiceType(serviceType2.get()).setCallCategory(callCategory2.get())
                    .setSubscriberType(subscriberType1.get()).setCharge_usd(2));
            chargeRepository.save(new Charge().setServiceType(serviceType2.get()).setCallCategory(callCategory2.get())
                    .setSubscriberType(subscriberType2.get()).setCharge_usd(2.5));
            chargeRepository.save(new Charge().setServiceType(serviceType3.get()).setCallCategory(callCategory1.get())
                    .setSubscriberType(subscriberType1.get()).setCharge_usd(1));
            chargeRepository.save(new Charge().setServiceType(serviceType3.get()).setCallCategory(callCategory1.get())
                    .setSubscriberType(subscriberType2.get()).setCharge_usd(1.5));
            chargeRepository.save(new Charge().setServiceType(serviceType3.get()).setCallCategory(callCategory2.get())
                    .setSubscriberType(subscriberType1.get()).setCharge_usd(5));
            chargeRepository.save(new Charge().setServiceType(serviceType3.get()).setCallCategory(callCategory2.get())
                    .setSubscriberType(subscriberType2.get()).setCharge_usd(6));
            

            //System.out.println("Query : " + cdrRecordRepository.findVolumePerDay());

            //Graph - Question 2
            Vertex A = new Vertex("A");
            Vertex B = new Vertex("B");
            Vertex C = new Vertex("C");
            Vertex D = new Vertex("D");
            Vertex E = new Vertex("E");
            Vertex F = new Vertex("F");
    
            // set the edges and weight
            A.adjacencies = new Edge[]{ new Edge(B, 2) , new Edge(C,5), new Edge(D,1) };
            B.adjacencies = new Edge[]{ new Edge(D, 2) , new Edge(C,3), new Edge(A,2) };
            C.adjacencies = new Edge[]{ new Edge(B, 3) , new Edge(D, 3), new Edge(E, 1) , new Edge(F,5), new Edge(A,5) };
            D.adjacencies = new Edge[]{ new Edge(A, 1), new Edge(B, 2) , new Edge(C,3), new Edge(E,1) };
            E.adjacencies = new Edge[]{ new Edge(D, 1) , new Edge(C,1), new Edge(F,2) };
            F.adjacencies = new Edge[]{ new Edge(C,5), new Edge(E,2) };
            Dijkstra dijkstra = new Dijkstra();
            dijkstra.computePaths(A);
            System.out.println("Distance to " + C + ": " + C.minDistance);
            List<Vertex> path = dijkstra.getShortestPathTo(C);
            System.out.println("Path: " + path);
        };
    }


    //Singleton
    @Bean
    public static CdrInmemStore cdrInmemStore() {
        CdrInmemStore cdrInmemStore = new CdrInmemStore();
        
        //Adding Service Types
        ServiceTypeM serviceTypeM1 = new ServiceTypeM("Voice");
        ServiceTypeM serviceTypeM2 = new ServiceTypeM("SMS");
        ServiceTypeM serviceTypeM3 = new ServiceTypeM("GPRS");
        cdrInmemStore.addServiceType(serviceTypeM1);
        cdrInmemStore.addServiceType(serviceTypeM2);
        cdrInmemStore.addServiceType(serviceTypeM3);

        //Adding CallCategories
        CallCategoryM callCategoryM1 = new CallCategoryM("Local");
        CallCategoryM callCategoryM2 = new CallCategoryM("Roaming");
        cdrInmemStore.addCallCategory(callCategoryM1);
        cdrInmemStore.addCallCategory(callCategoryM2);

        //Adding Subscriber Types
        SubscriberTypeM subscriberTypeM1 = new SubscriberTypeM("Postpaid");
        SubscriberTypeM subscriberTypeM2 = new SubscriberTypeM("Prepaid");
        cdrInmemStore.addSubscriberType(subscriberTypeM1);
        cdrInmemStore.addSubscriberType(subscriberTypeM2);
        
        //Adding Charges
        cdrInmemStore.addCharge("111",0.3);
        cdrInmemStore.addCharge("112",0.5);
        cdrInmemStore.addCharge("121",1.5);
        cdrInmemStore.addCharge("122",2.0);
        cdrInmemStore.addCharge("211",0.15);
        cdrInmemStore.addCharge("212",0.25);
        cdrInmemStore.addCharge("221",2.0);
        cdrInmemStore.addCharge("222",2.5);
        cdrInmemStore.addCharge("311",1.0);
        cdrInmemStore.addCharge("312",1.5);
        cdrInmemStore.addCharge("321",5.0);
        cdrInmemStore.addCharge("322",6.0);
        
        return cdrInmemStore;
    }

}
