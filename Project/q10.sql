create view dsview1 as
select serviceid,
  count(insno) inscount,
  avg(frate) afrate,
  min(tpvalue) mintp,
  max(tpvalue) maxtp,
  avg(tpvalue) avgtp,
  min(rtvalue) minrt,
  max(rtvalue) maxrt,
  avg(rtvalue) avgrt
  from dtprtlist
  group by serviceid;
