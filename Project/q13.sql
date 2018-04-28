create view dsview3 as
select a.*, b.wsdladdr, b.provname, b.country, b.price
from dsview2 a, servicelist b
where a.serviceid = b.serviceid;
