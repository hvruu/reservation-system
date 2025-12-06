package com.practice.reservation_system.rooms;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    private final RoomRepository repository;
    private final RoomMapper mapper;

    public RoomService(RoomRepository repository, RoomMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<Room> findByFilter(
            RoomSearchFilter filter
    ) {
        var pageSize = filter.pageSize() != null ? filter.pageSize() : 10;
        var pageNum = filter.pageNum() != null ? filter.pageNum() : 0;

        var pageable = Pageable.ofSize(pageSize).withPage(pageNum) ;
        List<RoomEntity> allEntities = repository.findByFilter(
                filter.id(),
                filter.capacity(),
                filter.type(),
                filter.priceStart(),
                filter.priceEnd(),
                filter.status(),
                pageable
        );

        return allEntities.stream()
                        .map(mapper::toDomain).toList();
    }

    public Room createRoom(
            Room roomToCreate
    ) {

        var entityToCreate = mapper.toEntity(roomToCreate);

        var roomToSave = repository.save(entityToCreate);

        return mapper.toDomain(roomToSave);
    }
}
