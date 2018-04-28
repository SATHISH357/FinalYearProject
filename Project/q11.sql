create view dmview1 as
select max(afrate) mafrate,
  max(mintp) mmintp,
  max(maxtp) mmaxtp,
  max(avgtp) mavgtp,
  max(minrt) mminrt,
  max(maxrt) mmaxrt,
  max(avgrt) mavgrt
  from dsview1;

