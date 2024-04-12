package com.pts.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "authority", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"tps_Username", "tps_Roleid"})
})
public class Authority implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tps_Id;
    @ManyToOne @JoinColumn(name = "tps_Username")
    private Account account;
    @ManyToOne  @JoinColumn(name = "tps_roleid")
    private Role role;
}
