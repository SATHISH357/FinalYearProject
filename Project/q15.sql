create view simview1 as
select serviceid, ((sweight-(select avg(sweight) from swlist))/ (select avg(sweight)from swlist)) sims,
  ((dweight-(select avg(dweight) from swlist))/ (select avg(dweight)from swlist)) simd
  from swlist;
