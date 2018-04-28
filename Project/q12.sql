create view dsview2 as
select serviceid,
  avg(opercount) opercount,
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
