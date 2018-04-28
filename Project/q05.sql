create view stprtview2 as
select a.*, b.tpvalue, b.rtvalue
from servicelist a, stprtlist b
where a.serviceid = b.serviceid;
