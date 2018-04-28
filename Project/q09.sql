create view smview1 as
select  max(tpvalue) smtp, max(rtvalue) smrt from stprtlist;
