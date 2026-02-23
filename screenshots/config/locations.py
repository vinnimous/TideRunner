"""
Fishing location presets used to drive the app into photogenic states.

Primary locations are Lockwood Folly Inlet and Frying Pan Tower — both
real, well-known NC fishing spots that produce excellent marine data.

Coordinates are (latitude, longitude).
"""

# ── Primary NC locations ───────────────────────────────────────────────────

NEUSE_RIVER_POINT = {
    "name": "Neuse River — Oriental, NC",
    "lat": 35.0196,
    "lon": -76.6989,
    "description": (
        "Classic NC inshore redfish water where the Neuse River meets Pamlico Sound "
        "near Oriental. Shallow grass flats, strong tidal current, and brackish "
        "conditions make this textbook redfish habitat."
    ),
    "best_species": "Redfish (Red Drum)",
    "type": "nearshore",
}

LOCKWOOD_FOLLY_INLET = {
    "name": "Lockwood Folly Inlet — NC Nearshore",
    "lat": 33.9075,
    "lon": -78.2183,
    "description": (
        "Classic NC nearshore inlet. Flounder, redfish, and speckled trout "
        "stack up on the moving tide. Strong tidal flow makes solunar timing critical."
    ),
    "best_species": "Redfish (Red Drum)",
    "type": "nearshore",
}

FRYING_PAN_TOWER = {
    "name": "Frying Pan Tower — NC Offshore",
    "lat": 33.4863,
    "lon": -77.5891,
    "description": (
        "Iconic offshore structure sitting atop Frying Pan Shoals, 34 miles out. "
        "Mahi, wahoo, and tuna congregate around this former Coast Guard tower."
    ),
    "best_species": "Mahi Mahi (Dolphin Fish)",
    "type": "offshore",
}

# ── Supporting locations for geographic variety ────────────────────────────

GULF_COAST = {
    "name": "Gulf Coast — Louisiana",
    "lat": 29.95,
    "lon": -89.10,
    "description": "Prime redfish and speckled trout territory. Rich in bayous and estuaries.",
    "best_species": "Redfish (Red Drum)",
    "type": "nearshore",
}

FLORIDA_KEYS = {
    "name": "Florida Keys — Islamorada",
    "lat": 24.92,
    "lon": -80.65,
    "description": "World-class flats fishing for bonefish and permit.",
    "best_species": "Cobia",
    "type": "nearshore",
}

# Primary location for hero / suitability score screenshots
PRIMARY_LOCATION = NEUSE_RIVER_POINT

# All locations in the order screenshots are captured
ALL_LOCATIONS = [
    NEUSE_RIVER_POINT,
    FRYING_PAN_TOWER,
    LOCKWOOD_FOLLY_INLET,
    GULF_COAST,
    FLORIDA_KEYS,
]

