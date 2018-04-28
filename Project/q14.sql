create view ssview1 as
select a.*, b.sweight, b.dweight
from servicelist a, swlist b
where a.serviceid = b.serviceid;
