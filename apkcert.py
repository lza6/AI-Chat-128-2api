"""解析 APK Signing Block (v2/v3) 提取签名证书 DER + SHA-256 base64 指纹"""
import hashlib
import base64
import struct
import sys


def find_block(data: bytes):
    magic = b"APK Sig Block 42"
    idx = data.rfind(magic)
    if idx < 0:
        return None
    size2 = struct.unpack_from("<Q", data, idx - 8)[0]
    start = idx + len(magic) - size2 - 8
    size1 = struct.unpack_from("<Q", data, start)[0]
    assert size1 == size2, (size1, size2)
    pairs_bytes = data[start + 8: idx - 8]
    pairs = {}
    off = 0
    while off < len(pairs_bytes):
        plen = struct.unpack_from("<Q", pairs_bytes, off)[0]
        pid = struct.unpack_from("<I", pairs_bytes, off + 8)[0]
        value = pairs_bytes[off + 12: off + 8 + plen]
        pairs[pid] = value
        off += 8 + plen
    return pairs


def certs_from_v3(v: bytes):
    # v3 block: signer sequence
    out = []
    signer_len = struct.unpack_from("<I", v, 0)[0]
    p = 4
    signed_data_len = struct.unpack_from("<I", v, p)[0]
    sd = v[p + 4: p + 4 + signed_data_len]
    q = 0
    digests_len = struct.unpack_from("<I", sd, q)[0]
    q += 4 + digests_len
    certs_len = struct.unpack_from("<I", sd, q)[0]
    q += 4
    certs = sd[q: q + certs_len]
    k = 0
    while k < len(certs):
        clen = struct.unpack_from("<I", certs, k)[0]
        out.append(certs[k + 4:k + 4 + clen])
        k += 4 + clen
    return out


def main(path):
    data = open(path, "rb").read()
    pairs = find_block(data)
    if not pairs:
        print("no signing block")
        return
    print("block ids:", [hex(k) for k in pairs])
    certs = []
    if 0x1B93AD61 in pairs:  # v3
        certs = certs_from_v3(pairs[0x1B93AD61])
        print("v3 certs:", len(certs))
    if not certs and 0x7109871A in pairs:  # v2
        v = pairs[0x7109871A]
        signer_len = struct.unpack_from("<I", v, 0)[0]
        p = 4
        sd_len = struct.unpack_from("<I", v, p)[0]
        sd = v[p + 4:p + 4 + sd_len]
        q = 0
        digests_len = struct.unpack_from("<I", sd, q)[0]
        q += 4 + digests_len
        certs_len = struct.unpack_from("<I", sd, q)[0]
        q += 4
        c = sd[q:q + certs_len]
        k = 0
        while k < len(c):
            clen = struct.unpack_from("<I", c, k)[0]
            certs.append(c[k + 4:k + 4 + clen])
            k += 4 + clen
        print("v2 certs:", len(certs))
    for i, der in enumerate(certs):
        print(f"cert[{i}] len={len(der)} sha256_b64={base64.b64encode(hashlib.sha256(der).digest()).decode()} "
              f"sha1={hashlib.sha1(der).hexdigest()}")
        open(f"cert_{i}.der", "wb").write(der)


if __name__ == "__main__":
    main(sys.argv[1] if len(sys.argv) > 1 else "aichat133.apk")
