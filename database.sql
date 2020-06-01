create table videojuego(sku int not null primary key,nombre varchar(255) not null,fecha_lanzamiento int, no_jugadores int,categoria varchar(255) not null,desarrollador varchar(255), enlinea BOOLEAN not null, portada varchar(255) not null, existencias int not null,precio decimal(8,2) not null);

create table venta(folio int not null AUTO_INCREMENT primary key,fecha datetime not null,total decimal(8,2) not null);

create table ventaxvideojuego(folioVenta int not null,skuVideojuego int not null,cantidad int not null);
alter table ventaxvideojuego add primary key(folioVenta,skuVideojuego);
alter table ventaxvideojuego add foreign key (folioVenta) references venta(folio) on update cascade on delete cascade;
alter table ventaxvideojuego add foreign key (skuVideojuego) references videojuego(sku) on update cascade on delete cascade;


delimiter //

create procedure addVideojuego(sku int,nombre varchar(255),fecha_lanzamiento int,no_jugadores int,categoria varchar(255),desarrollador varchar(255),enlinea BOOLEAN,portada varchar(255),existencias int,precio decimal(8,2))
begin
	insert into videojuego(sku,nombre,fecha_lanzamiento,no_jugadores,categoria,desarrollador,enlinea,portada,existencias,precio) values 		(sku,nombre,fecha_lanzamiento,no_jugadores,categoria,desarrollador,enlinea,portada,existencias,precio);
end//

create procedure listarVideojuego()
begin
	select nombre from videojuego;
end//

create procedure delVideojuego(snombre varchar(255))
begin
	delete from videojuego where nombre=snombre;
end//

create procedure listarAttVideojuegos(snombre varchar(255))
begin
	select sku,nombre,fecha_lanzamiento,no_jugadores,desarrollador,existencias,precio from videojuego where nombre=snombre;
end //

create procedure edVideojuego(ssku int,nnombre varchar(255),nfecha_lanzamiento int,nno_jugadores int,ncategoria varchar(255),ndesarrollador varchar(255),nenlinea BOOLEAN,nportada varchar(255),nexistencias int,nprecio decimal(8,2))
begin
	update videojuego set nombre=nnombre,fecha_lanzamiento=nfecha_lanzamiento,no_jugadores=nno_jugadores,
	categoria=ncategoria,desarrollador=ndesarrollador,enlinea=nenlinea,portada=nportada,existencias=nexistencias,
 	precio=nprecio where sku=ssku;
end //

create procedure crearListaVideojuegos()
begin
	select * from videojuego;
end //

create procedure insertarVenta(ntotal decimal(8,2))
begin
	insert into venta(fecha,total) values(now(),ntotal);
end //

create procedure insertarDetalleventa(nskuVideojuego int,ncantidad int)
begin
	insert into ventaxvideojuego(folioVenta,skuVideojuego,cantidad) values ((SELECT folio from venta order by folio desc limit  1),nskuVideojuego,ncantidad);
	update videojuego set existencias=existencias-ncantidad where sku=nskuVideojuego;
end //
delimiter ;


