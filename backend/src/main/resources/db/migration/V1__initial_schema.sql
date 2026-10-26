CREATE TABLE clients (
  id UUID PRIMARY KEY,
  full_name VARCHAR(200) NOT NULL,
  phone VARCHAR(32) NOT NULL,
  email VARCHAR(254),
  district VARCHAR(120) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE TABLE loan_applications (
  id UUID PRIMARY KEY,
  client_id UUID NOT NULL REFERENCES clients(id),
  principal NUMERIC(18,2) NOT NULL CHECK (principal > 0),
  currency CHAR(3) NOT NULL DEFAULT 'MZN' CHECK (currency = 'MZN'),
  status VARCHAR(32) NOT NULL DEFAULT 'SUBMITTED' CHECK (status IN ('SUBMITTED','UNDER_REVIEW','APPROVED','REJECTED')),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE TABLE loans (
  id UUID PRIMARY KEY,
  application_id UUID NOT NULL UNIQUE REFERENCES loan_applications(id),
  principal NUMERIC(18,2) NOT NULL CHECK (principal > 0),
  outstanding NUMERIC(18,2) NOT NULL CHECK (outstanding >= 0),
  annual_interest_rate NUMERIC(7,4) NOT NULL CHECK (annual_interest_rate >= 0),
  status VARCHAR(32) NOT NULL CHECK (status IN ('ACTIVE','OVERDUE','CLOSED')),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE TABLE payments (
  id UUID PRIMARY KEY,
  loan_id UUID NOT NULL REFERENCES loans(id),
  amount NUMERIC(18,2) NOT NULL CHECK (amount > 0),
  external_reference VARCHAR(150) UNIQUE,
  recorded_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE TABLE audit_events (
  id UUID PRIMARY KEY,
  actor VARCHAR(150) NOT NULL,
  action VARCHAR(100) NOT NULL,
  entity_type VARCHAR(100) NOT NULL,
  entity_id UUID,
  occurred_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_applications_client ON loan_applications(client_id);
CREATE INDEX idx_loans_status ON loans(status);
CREATE INDEX idx_payments_loan ON payments(loan_id);
CREATE INDEX idx_audit_occurred ON audit_events(occurred_at DESC);
