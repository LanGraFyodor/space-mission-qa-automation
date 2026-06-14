-- Initial test data for Space Mission Management API

-- Missions
INSERT INTO missions (name, description, status, launch_date) VALUES
('Starlink-1', 'First batch of Starlink internet satellites', 'ACTIVE', '2024-01-15');
INSERT INTO missions (name, description, status, launch_date) VALUES
('Lunar Gateway', 'Lunar orbital station construction mission', 'PLANNED', '2025-06-01');
INSERT INTO missions (name, description, status, launch_date) VALUES
('Mars Observer', 'Mars surface observation and data relay', 'PLANNED', '2026-03-20');

-- Satellite Groups
INSERT INTO satellite_groups (name, purpose, mission_id) VALUES
('Starlink Batch A', 'Internet coverage for North America', 1);
INSERT INTO satellite_groups (name, purpose, mission_id) VALUES
('Lunar Relay Network', 'Communication relay around the Moon', 2);

-- Satellites
INSERT INTO satellites (name, type, orbit_type, status, group_id) VALUES
('SL-001', 'COMMUNICATION', 'LEO', 'ACTIVE', 1);
INSERT INTO satellites (name, type, orbit_type, status, group_id) VALUES
('SL-002', 'COMMUNICATION', 'LEO', 'ACTIVE', 1);
INSERT INTO satellites (name, type, orbit_type, status, group_id) VALUES
('LR-001', 'COMMUNICATION', 'GEO', 'INACTIVE', 2);
INSERT INTO satellites (name, type, orbit_type, status, group_id) VALUES
('MO-001', 'OBSERVATION', 'MEO', 'ACTIVE', NULL);
